package com.tabusearch;

import com.example.icobackend.models.AlgorithmRequest;
import com.tabusearch.components.Solution;

public interface Solver {
    void run(AlgorithmRequest algorithmRequest);

    void setSolution(Solution solution);

    Solution getSolution();
}
