describe("End to end test", () => {

describe('Admin End to End path test', () => {
  it('Should display create button for admin', () => {
    cy.visit('/login')
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.url().should('include', '/sessions')
    cy.get('button[routerLink="create"]').should('be.visible');
  });

  it('Allows admin to create a new session', () => {
    cy.get('button[routerLink="create"]').click()
    cy.url().should('include', '/create');

    cy.get('.create-session-title').should('exist');
    cy.get('input[formControlName="name"]').should('have.value', '');
    cy.get('input[formControlName="date"]').should('have.value', '');
    cy.get('textarea[formControlName="description"]').should('have.value', '');

    cy.get('input[formControlName="name"]').type('Yoga Session 1');
    cy.get('input[formControlName="date"]').type('2024-02-01');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('textarea[formControlName="description"]').type('description session');

    cy.get('form', {timeout : 10000}).submit();

    cy.url().should('include', '/sessions');
    cy.contains('Yoga Session 1').should('exist');
  });

  it('Allows admin to edit a session', () => {
    cy.url().should('include', '/sessions');
    cy.get('mat-card-title', { timeout: 10000 }).should('exist');
    cy.contains('Yoga Session 1').parents('.item').find('button').contains('Edit').click();

    cy.get('input[formControlName="name"]').clear().type('Session Updated');
    cy.get('input[formControlName="date"]').type('2024-02-01');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('textarea[formControlName="description"]').clear().type('description session updated');

    cy.get('form', { timeout : 10000 }).submit();

    cy.url().should('include', '/sessions');
    cy.contains('Session Updated', { timeout: 10000}).should('exist');
  });

  it('Allows admin to view details and delete a session', () => {
    cy.get('mat-card-title', { timeout: 10000 }).should('exist');
    cy.url().should('include', '/sessions');

    cy.contains('mat-card-title', 'Session Updated', { timeout: 10000 })
      .closest('mat-card')
      .find('button')
      .contains('Detail')
      .click();

    cy.url().should('include', '/detail');
    cy.get('button.details_delete_button', { timeout : 10000}).contains('Delete').click()

    cy.url().should('include', '/sessions');
    cy.contains('Session Updated').should('not.exist');
  });

  it('Shows account page and logout', () => {
    cy.get('[routerLink="me"]', { timeout: 10000 }).should('exist').click();
    cy.url().should('include', '/me');

    cy.contains('You are admin').should('exist');

    cy.get('span').contains('Logout').click();
    cy.url().should('eq', 'http://localhost:4200/');
  });
});
  describe('User End to End path test', () => {

    it('Should register successful', ()  => {
      cy.visit('/register')
      cy.get('input[formControlName="firstName"]').type("Theo8")
      cy.get('input[formControlName="lastName"]').type("Kennel6")
      cy.get('input[formControlName="email"]').type("endToEnd33@test.com")
      cy.get('input[formControlName="password"]').type(`${"123456"}{enter}{enter}`)
    });

    it('Should login successful',() => {
      cy.url().should('include', '/login');
      cy.get('input[formControlName=email]').type("endToEnd33@test.com")
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
      cy.get("mat-card-content p", { timeout: 10000 }).contains('Email:').should('contain', 'Email: endToEnd33@test.com');
      cy.get('button').contains('span.ml1', 'Detail').should('exist').click();
      cy.url().should('eq', 'http://localhost:4200/');
    });
  });
});
