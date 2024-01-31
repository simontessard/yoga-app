describe('Register spec', () => {
  it('is successful', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', { statusCode: 200 });

    cy.get('input[formControlName=firstName]').type('jean');
    cy.get('input[formControlName=lastName]').type('durand');
    cy.get('input[formControlName=email]').type('jean@durand.com');
    cy.get('input[formControlName=password]').type('newpass{enter}');

    cy.url().should('include', '/login');
  });

  it('should disable submit button if a field is empty', () => {
    cy.visit('/register');
    cy.get('input[formControlName=firstName]').type('jean');
    cy.get('input[formControlName=lastName]').type('durand');
    cy.get('input[formControlName=email]').clear();
    cy.get('input[formControlName=password]').type('wrongpass');
    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid');
    cy.get('button[type=submit]').should('be.disabled');
  });

  it('should return an error if email is already used', () => {
    cy.visit('/register');

    cy.intercept('POST', '/api/auth/register', { statusCode: 400 });

    cy.get('input[formControlName=firstName]').type('jean');
    cy.get('input[formControlName=lastName]').type('durand');
    cy.get('input[formControlName=email]').type('jean@durand.com');
    cy.get('input[formControlName=password]').type('wrongpass{enter}');
    cy.get('.error').should('be.visible');
  });
});
