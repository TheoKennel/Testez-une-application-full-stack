describe('Admin End to End path test', () => {
  before(() => {
    cy.visit('/login')
    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.url().should('include', '/sessions')
  });

  it('Should display create button for admin', () => {
    cy.get('button[routerLink="create"]').should('be.visible');
  });

  it('Allows admin to create a new session', () => {
    cy.get('button[routerLink="create"]').click()
    cy.url().should('include', '/create');

    cy.get('input[formControlName="name"]').type('Yoga Session New1');
    cy.get('input[formControlName="date"]').type('2024-02-01');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('textarea[formControlName="description"]').type('description session');
    cy.get('form').submit();

    cy.url().should('include', '/sessions');
    cy.contains('Yoga Session New1').should('exist');
  });

  it('Allows admin to edit a session', () => {
    cy.url().should('include', '/sessions');
    cy.get('mat-card-title', { timeout: 10000 }).should('exist');
    cy.contains('Yoga Session New1').parents('.item').find('button').contains('Edit').click();

    cy.get('input[formControlName="name"]').clear().type('Session Updated1');
    cy.get('input[formControlName="date"]').type('2024-02-01');
    cy.get('mat-select[formControlName="teacher_id"]').click();
    cy.get('mat-option').contains('Margot DELAHAYE').click();
    cy.get('textarea[formControlName="description"]').clear().type('description session updated');

    cy.get('form').submit();

    cy.url().should('include', '/sessions');
    cy.contains('Session Updated1', { timeout: 10000}).should('exist');
  });

  it('Allows admin to view details and delete a session', () => {
    cy.get('mat-card-title', { timeout: 10000 }).should('exist');
    cy.url().should('include', '/sessions');

    cy.contains('Session Updated1').parents('.mat-card').find('button').contains('Detail').click();
    cy.url().should('include', '/detail');
    cy.get('button.details_delete_button').contains('Delete').should('exist').click()
    cy.url().should('include', '/sessions');
    cy.contains('Session Updated1').should('not.exist');
  });

  it('Shows account page and logout', () => {
    cy.get('[routerLink="me"]', { timeout: 10000 }).should('exist').click();
    cy.url().should('include', '/me');

    cy.contains('You are admin').should('exist');

    cy.get('span').contains('Logout').click();
    cy.url().should('eq', 'http://localhost:4200/');
  });
});
