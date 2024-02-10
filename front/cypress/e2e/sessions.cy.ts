describe('Sessions', () => {
  beforeEach(() => {
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

    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 2,
          firstName: 'Hélène',
          lastName: 'THIERCELIN',
        },
      ],
    });

    cy.get('input[formControlName=email]').type('yoga@studio.com');
    cy.get('input[formControlName=password]').type(
      `${'test!1234'}{enter}{enter}`
    );

    cy.url().should('include', '/sessions');
  });

  it('displays sessions', () => {
    cy.get('.item').should('have.length', 2);
    cy.get('.item')
      .first()
      .within(() => {
        cy.get('mat-card-title').should('contain', 'Session 1');
        cy.get('mat-card-subtitle').should('contain', 'March 31, 2000');
        cy.get('mat-card-content p').should('contain', '21');
      });
    cy.get('.item')
      .last()
      .within(() => {
        cy.get('mat-card-title').should('contain', 'Yoga');
        cy.get('mat-card-subtitle').should('contain', 'January 4, 2024');
        cy.get('mat-card-content p').should('contain', 'Séance yoga');
      });
  });

  it('displays session details and delete', () => {
    // Intercept the GET request to the API and provide a mock response
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Session 1',
        date: '2000-03-31T00:00:00.000+00:00',
        teacher_id: 1,
        description: '21',
        users: [],
        createdAt: '2024-01-20T19:12:50',
        updatedAt: '2024-01-25T16:49:23',
      },
    }).as('session1');

    cy.intercept('GET', '/api/teacher/1', {
      body: {
        id: 1,
        lastName: 'THIERCELIN',
        firstName: 'Hélène',
        createdAt: '2024-01-20T19:12:50',
        updatedAt: '2024-01-25T16:49:23',
      },
    }).as('teacher1');

    // Navigate to the details page of the first session
    cy.get('.item')
      .first()
      .within(() => {
        cy.get('button[data-testid=detail-button]').click();
      });
    cy.url().should('include', '/sessions/detail/1');

    // Check that the session details are displayed correctly
    cy.get('mat-card-title h1').should('contain', 'Session 1');
    cy.get('mat-card-subtitle span').should('contain', 'Hélène THIERCELIN');
    cy.get('mat-card-content .description').should('contain', '21');
    cy.get('mat-card-content div[fxLayoutAlign="start center"] span').should(
      'contain',
      '0 attendees'
    );

    cy.get('button[data-testid=delete-button]').click();
  });

  it('creates a session', () => {
    cy.get('button[data-testid=create-button-admin]').click();
    cy.url().should('include', '/sessions/create');

    cy.get('.create mat-card-title').contains('Create session').should('exist');

    cy.get('input[formControlName=name]').type('New Session');
    cy.get('input[formControlName=date]').type('2022-12-31');
    cy.get('mat-select[formControlName="teacher_id"]')
      .click()
      .get('mat-option')
      .contains('Hélène THIERCELIN')
      .click();
    cy.get('textarea[formControlName=description]').type(
      'This is a new session.'
    );
    cy.get('button[type=submit]').should('not.be.disabled');
    cy.get('button[type=submit]').click();

    // cy.url().should('include', '/sessions');
    // cy.get('.item')
    //   .last()
    //   .within(() => {
    //     cy.get('mat-card-title').should('contain', 'New Session');
    //   });
  });

  it('edits a session', () => {
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: 'Session 1',
        date: '2000-03-31T00:00:00.000+00:00',
        teacher_id: 1,
        description: '21',
        users: [],
        createdAt: '2024-01-20T19:12:50',
        updatedAt: '2024-01-25T16:49:23',
      },
    });

    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          firstName: 'Margot',
          lastName: 'DELAHAYE',
        },
      ],
    });

    cy.get('.item')
      .first()
      .find('button[data-testid=edit-button-admin]')
      .click();
    cy.url().should('include', '/sessions/update');

    cy.get('input[formControlName=name]').clear().type('Update Session Name');
    cy.get('textarea[formControlName=description]').clear();
    cy.get('button[type=submit]').should('be.disabled');
    cy.get('textarea[formControlName=description]').type(
      'This is an updated session.'
    );
    cy.get('button[type=submit]').click();

    cy.url().should('include', '/sessions');
    cy.visit('/sessions');
  });
});
