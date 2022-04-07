package com.efrei.thdesgrphs.automaton;

import com.efrei.thdesgrphs.io.FileReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AutomatonTest {

    private Automaton testAutomaton = FileReader.readFile("/Users/benjaminlesieux/Desktop/Théorie des graphes/src/main/java/com/efrei/thdesgrphs/files/C02.txt");

    @BeforeEach
    void setUp() {
        testAutomaton.loadSuccessors();
    }

    @Test
    @DisplayName("Adding a state to the cloned automaton")
    void testAddStateClone() {
        Automaton clone = testAutomaton.clone();
        clone.getStates().add(new State(clone.getStates().size() + 2, 1, null, null));

        assertNotEquals(clone.getStates().size(), testAutomaton.getStates().size());
    }

    @Test
    @DisplayName("Remove a state to the cloned automaton")
    void testRemoveStateClone() {
        Automaton clone = testAutomaton.clone();
        clone.getStates().remove(0);

        assertNotEquals(clone.getStates().size(), testAutomaton.getStates().size());
    }

    @Test
    @DisplayName("Remove a predecessor from the cloned automaton")
    void removePredecessorState() {
        Automaton clone = testAutomaton.clone();
        clone.getByID(3).predecessors().remove(0);

        assertNotEquals(clone.getByID(3).predecessors().size(), testAutomaton.getByID(3).predecessors().size());
        assertNotEquals(clone.getByID(3).predecessors(), testAutomaton.getByID(3).predecessors());
    }

    @Test
    @DisplayName("Add a predecessor from the cloned automaton")
    void addPredecessorState() {
        Automaton clone = testAutomaton.clone();
        clone.getByID(3).predecessors().add(12);

        assertNotEquals(clone.getByID(3).predecessors().size(), testAutomaton.getByID(3).predecessors().size());
        assertNotEquals(clone.getByID(3).predecessors(), testAutomaton.getByID(3).predecessors());
    }

    @Test
    @DisplayName("Remove a successor from the cloned automaton")
    void removeSuccessor() {
        Automaton clone = testAutomaton.clone();
        State s = clone.getByID(3).successors().get(0);
        clone.getByID(3).successors().remove(0);
        s.predecessors().remove(s.predecessors().indexOf(3));
    }
}