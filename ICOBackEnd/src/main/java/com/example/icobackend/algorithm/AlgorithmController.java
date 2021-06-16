package com.example.icobackend.algorithm;

import com.example.icobackend.models.*;
import com.tabusearch.TabuSearchAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@CrossOrigin
@RequiredArgsConstructor
public class AlgorithmController {

    @ResponseBody
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    public AlgorithmResponse getSolutionsEvaluations(@RequestBody AlgorithmRequest algorithmRequest) {

        //algorithmRequest = PerformanceTests.generateTest(3, 20);

        JspritVRPAlgorithm jspritVRPAlgorithm = new JspritVRPAlgorithm();
        AlgorithmResponse jspritResponse = jspritVRPAlgorithm.simulateAuto(algorithmRequest);

        TabuSearchAlgorithm tabuSearchAlgorithm = new TabuSearchAlgorithm();
        AlgorithmResponse tabuResponse = tabuSearchAlgorithm.vrpSearchAlgoAuto(algorithmRequest, false);

        return jspritResponse.getSolutionCost() < tabuResponse.getSolutionCost() ? jspritResponse : tabuResponse;
    }
}
