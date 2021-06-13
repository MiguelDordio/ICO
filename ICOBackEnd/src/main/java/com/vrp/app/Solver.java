package com.vrp.app;

import com.example.icobackend.models.AlgorithmRequest;
import com.vrp.app.components.Solution;

public interface Solver {
    void run(AlgorithmRequest algorithmRequest);

    void setSolution(Solution solution);

    Solution getSolution();
}
