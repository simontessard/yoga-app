describe('Login spec', () => {
  it('login successfull as admin', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true,
      },
    });

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Session 1',
          date: '2000-03-31T00:00:00.000+00:00',
          teacher_id: 1,
          description: '21',
          users: [],
          createdAt: '2024-01-20T19:12:50',
          updatedAt: '2024-01-25T16:49:23',
        },
        {
          id: 2,
          name: 'Yoga',
          date: '2024-01-04T00:00:00.000+00:00',
          teacher_id: 2,
          description: 'Séance yoga',
          users: [],
          createdAt: '2024-01-24T23:08:50',
          updatedAt: '2024-01-24T23:08:50',
        },
      ],
    });

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    cy.url().should('include', '/sessions');

    cy.get('button[data-testid=create-button-admin]').should('be.visible');
    cy.get('button[data-testid=edit-button-admin]').should('be.visible');
  });

  it('logout successful', () => {
    cy.url().should('include', '/sessions');
    cy.get('span[data-testid=logout-button]').click();
  });

  it('login successfull (not admin)', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: false,
      },
    });

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Session 1',
          date: '2000-03-31T00:00:00.000+00:00',
          teacher_id: 1,
          description: '21',
          users: [],
          createdAt: '2024-01-20T19:12:50',
          updatedAt: '2024-01-25T16:49:23',
        },
      ],
    });

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    cy.url().should('include', '/sessions');

    cy.get('button[data-testid=create-button-admin]').should('not.exist');
    cy.get('button[data-testid=edit-button-admin]').should('not.exist');
  });
});
