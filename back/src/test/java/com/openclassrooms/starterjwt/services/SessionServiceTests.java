package com.openclassrooms.starterjwt.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionServiceTests {
    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    private SessionService sessionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sessionService = new SessionService(sessionRepository, userRepository);
    }

    @Test
    public void testCreate() {
        Session session = new Session();
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        Session created = sessionService.create(session);
        assertEquals(session, created);
    }

    @Test
    public void testDelete() {
        doNothing().when(sessionRepository).deleteById(anyLong());
        sessionService.delete(1L);
        verify(sessionRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testFindAll() {
        List<Session> sessions = new ArrayList<>();
        when(sessionRepository.findAll()).thenReturn(sessions);
        List<Session> result = sessionService.findAll();
        assertEquals(sessions, result);
    }

    @Test
    public void testGetById() {
        Session session = new Session();
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        Session result = sessionService.getById(1L);
        assertEquals(session, result);
    }

    @Test
    public void testUpdate() {
        Session session = new Session();
        when(sessionRepository.save(any(Session.class))).thenReturn(session);
        Session updated = sessionService.update(1L, session);
        assertEquals(session, updated);
    }

    @Test
    public void testParticipateUserNotFound() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(new Session()));
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    public void testParticipateAlreadyParticipating() {
        Session session = new Session();
        session.setUsers(new ArrayList<>());
        User user = new User();
        user.setId(1L);
        session.getUsers().add(user);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    public void testNoLongerParticipateNotParticipating() {
        Session session = new Session();
        session.setUsers(new ArrayList<>());
        User user = new User();
        user.setId(1L);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    @Test
    public void testNoLongerParticipateSuccess() {
        Session session = new Session();
        session.setUsers(new ArrayList<>());
        User user = new User();
        user.setId(1L);
        session.getUsers().add(user);
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.of(session));
        sessionService.noLongerParticipate(1L, 1L);
        assertFalse(session.getUsers().contains(user)); // Verify that the user was removed from the session
        verify(sessionRepository, times(1)).save(session); // Verify that the session was saved
    }

    @Test
    public void testNoLongerParticipateSessionNotFound() {
        when(sessionRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }
}
