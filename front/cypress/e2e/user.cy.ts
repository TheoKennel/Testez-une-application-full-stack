describe('User End to End path test', () => {

  it('Should register successful', ()  => {
    cy.visit('/register')
    cy.get('input[formControlName="firstName"]').type("Theo8")
    cy.get('input[formControlName="lastName"]').type("Kennel6")
    cy.get('input[formControlName="email"]').type("endToEndFinal@test.com")
    cy.get('input[formControlName="password"]').type(`${"123456"}{enter}{enter}`)
  });

  it('Should login successful',() => {
    cy.url().should('include', '/login');
    cy.get('input[formControlName=email]').type("endToEndFinal@test.com")
    cy.get('input[formControlName=password]').type(`${"123456"}{enter}{enter}`)
    cy.url().should('include', '/sessions')
  });

  it('Should display Rentals with title and only Details button', () => {
    cy.get('mat-card-title', { timeout: 10000 }).should(($title) => {
      expect($title).to.contain("Rentals available");
      expect($title).to.contain("Yogasession");
    });

    cy.contains("Yogasession").parents('.item').find('button').should(($button) => {
      expect($button).to.not.contain('Edit');
    });
    cy.contains("Yogasession").parents('.item').find('button').contains('Detail').should('exist').click();
  });

  it('Should display details, add participation, update attendees and then go back', () => {
    cy.url().should('include', '/detail')
    cy.get('mat-card-title h1', { timeout: 10000 }).should('contain', 'Yogasession');

    cy.get('.details_delete_button').should('not.exist');
    cy.get('.details_remove_participation-button').should('not.exist');

    cy.contains('button', 'Participate', { timeout : 10000}).click();
    cy.get('.ml1').contains("attendees", { timeout: 10000 }).should('contain', "1 attendees");

    cy.get('button').should(($button) => {
      expect($button).to.not.contain('Participate');
    })

    cy.contains('button', 'Do not participate', { timeout : 10000}).should('exist').click()
    cy.get('.ml1').contains("attendees", { timeout: 10000 }).should('contain', "0 attendees")

    cy.get('button[mat-icon-button]').contains('arrow_back').click();
    cy.url().should('include', '/sessions');
  });

  it('Should show account details, and delete it on click', () => {
    cy.get('[routerLink="me"]', { timeout: 10000 }).should('exist').click();
    cy.url().should('include', '/me');
    cy.contains('You are admin').should('not.exist');
    cy.get("mat-card-content p", { timeout: 10000 }).contains('Email:').should('contain', 'Email: endToEndFinal@test.com');
    cy.get('button').contains('span.ml1', 'Detail').should('exist').click();
    cy.url().should('eq', 'http://localhost:4200/');
  });

});
