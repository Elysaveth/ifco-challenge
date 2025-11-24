package com.ifco.challenge.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.ifco.challenge.domain.exception.DuplicateRecordException;

class DuplicateRecordExceptionTest {

    @Test
    void duplicateRecordException_hasExpectedMessage_andIsException() {
        DuplicateRecordException ex = new DuplicateRecordException();
        assertNotNull(ex.getMessage());
        assertTrue(ex instanceof Exception);
        assertEquals("There was a duplicate entry saved on database", ex.getMessage());
    }
}
