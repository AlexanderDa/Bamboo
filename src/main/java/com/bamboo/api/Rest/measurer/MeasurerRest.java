package com.bamboo.api.Rest.measurer;

import com.bamboo.api.Router;
import com.bamboo.api.dto.MeasurerDto;
import com.bamboo.api.dto.UptakeDto;
import com.bamboo.api.method.MeasurerDtoMethod;
import com.bamboo.api.method.UptakeDtoMethod;
import com.bamboo.model.entity.Measurer;
import com.bamboo.model.entity.Uptake;
import com.bamboo.model.method.MeasurerImpl;
import com.bamboo.model.method.SapImpl;
import com.bamboo.model.method.StatusImpl;
import com.bamboo.model.method.UptakeImpl;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "MeasurerRest", urlPatterns = {"/api/measurer/*"})
public class MeasurerRest extends HttpServlet {
    private final Router router = new Router("measurer");
    private final Gson gson = new Gson();
    private Map<String, Object> map = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        router.setRequest(request);
        MeasurerDtoMethod measurerDtoMethod = new MeasurerDtoMethod();
        UptakeDtoMethod uptakeDtoMethod = new UptakeDtoMethod();
        String responseJson = "";


        switch (router.getRoute()) {
            case "/measurer":
                try {
                    List<MeasurerDto> list = new ArrayList<>();
                    for (MeasurerDto measurerDto : measurerDtoMethod.find()) {
                        measurerDto.setUptakes(uptakeDtoMethod.findByMeasurer(measurerDto.getId()));
                        list.add(measurerDto);
                    }
                    responseJson = gson.toJson(list);
                } catch (Exception ex) {
                    response.sendError(400);
                    map.put("error", ex.getMessage());
                    responseJson = gson.toJson(map);
                }
                break;
            case "/measurer/findone":
                try {
                    int measurerId = Integer.parseInt(router.getParam("findone"));

                    MeasurerDto measurerDto = measurerDtoMethod.findById(measurerId);
                    if (measurerDto != null)
                        measurerDto.setUptakes(uptakeDtoMethod.findByMeasurer(measurerDto.getId()));
                    responseJson = gson.toJson(measurerDto);

                } catch (Exception ex) {
                    response.sendError(400);
                    map.put("error", "" + ex.getMessage());
                    responseJson = gson.toJson(map);

                }
                break;
            default:
                responseJson = "{\"msg\":\"Ruta no valida\"}";
                break;
        }

        response.getWriter().write(responseJson);
    }
}