package com.efrei.thdesgrphs.automaton;

import java.util.List;

public record State(int id, int cost, List<Integer> predecessors) {}
