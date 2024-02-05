describe('User End to End path test', () => {
  it('Should register successful', ()  => {
    cy.visit('/register')
    cy.get('input[formControlName="firstName"]').type("Theo")
    cy.get('input[formControlName="lastName"]').type("Kennel")
    cy.get('input[formControlName="email"]').type("endToEnd@test.com")
    cy.get('formControlName="password"').type(`${"123456"}{enter}{enter}`)
  })
  it('Should login successful',() => {
    cy.url().should('include', '/login');
    cy.get('input[formControlName=email]').type("endToEnd@test.com")
    cy.get('input[formControlName=password]').type(`${"123456"}{enter}{enter}`)
    cy.url().should('include', '/sessions')
  })

})
