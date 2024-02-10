describe('me spec', () => {
  it('Login successfull', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'simon',
        firstName: 'test',
        lastName: 'test',
        admin: true,
      },
    });

    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Workout',
          description: 'A workout session',
          date: '2023-12-30T00:00:00.000+00:00',
          teacher_id: 1,
          users: [],
        },
      ],
    });

    cy.get('input[formControlName=email]').type('test@test.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );
  });

  it('should display that you are an admin', () => {
    cy.url().should('include', '/sessions');

    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        firstName: 'Test',
        lastName: 'Test',
        createdAt: '2023-01-30T17:34:44',
        updatedAt: '2023-01-30T17:34:44',
        email: 'yoga@studio.com',
        admin: true,
      },
    });
    cy.get('[routerLink=me]').click();
    cy.url().should('include', '/me');

    // Check that the admin message is displayed
    cy.contains('p', 'You are admin');
  });

  it('should check user profile data and delete', () => {
    cy.get('[routerLink=sessions]').click();
    cy.url().should('include', '/sessions');

    cy.intercept('GET', '/api/user/1', {
      body: {
        id: 1,
        firstName: 'Test',
        lastName: 'Test',
        createdAt: '2023-01-30T17:34:44',
        updatedAt: '2023-01-30T17:34:44',
        email: 'yoga@studio.com',
        admin: false,
      },
    });
    cy.get('[routerLink=me]').click();
    cy.url().should('include', '/me');

    cy.contains('p', 'Name: Test TEST');
    cy.contains('p', 'Email: yoga@studio.com');

    cy.contains('p', 'Create at: January 30, 2023');
    cy.contains('p', 'Last update: January 30, 2023');

    cy.intercept('DELETE', '/api/user/1', {
      status: 200,
    });
    cy.get('button[data-testid=delete-button]').click();
    cy.clock(); // To prevent the snackbar to disappear
    cy.contains(
      '.mat-simple-snack-bar-content',
      'Your account has been deleted !'
    );
  });
});
