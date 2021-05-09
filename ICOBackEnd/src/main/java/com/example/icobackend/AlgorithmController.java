package com.example.icobackend;

import com.example.icobackend.models.AlgorithmRequest;
import com.example.icobackend.models.AlgorithmResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@CrossOrigin
@RequiredArgsConstructor
public class AlgorithmController {

    @ResponseBody
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes=MediaType.APPLICATION_JSON_VALUE)
    public AlgorithmResponse getSolutionsEvaluations(@RequestBody AlgorithmRequest algorithmRequest) {
        // fazer coisas com o request
        System.out.println(algorithmRequest.toString());
        List<String> destinies = new ArrayList<>();
        destinies.add("City1");
        destinies.add("City2");
        return new AlgorithmResponse(destinies);
    }
}
