describe('Page not found', () => {
  it('should appears', () => {
    cy.visit('/non-existing-url');
    cy.get('h1').should('contain', 'Page not found !');
  });
});
