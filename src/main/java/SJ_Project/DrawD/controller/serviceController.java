package SJ_Project.DrawD.controller;

import SJ_Project.DrawD.DTO.authDTO.identificationDTO;
import SJ_Project.DrawD.DTO.dataDTO.responseSelectedDiagramDTO;
import SJ_Project.DrawD.DTO.dataDTO.saveRequestDTO;
import SJ_Project.DrawD.DTO.dataDTO.selectDiagramRequestDTO;
import SJ_Project.DrawD.service.auth.jwtUtil;
import SJ_Project.DrawD.service.data.diagramUtil;
import SJ_Project.DrawD.service.repository.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class serviceController {
    private final diagramUtil diagramUtil;

    public serviceController(diagramUtil diagramUtil) {
        this.diagramUtil = diagramUtil;
    }

    @PostMapping("/save/Diagram")
    public ResponseEntity<String> saveDiagram(@RequestBody saveRequestDTO saveRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()) {
            identificationDTO idDTO = (identificationDTO) authentication.getPrincipal();

            try {
                if(diagramUtil.checkValidDiagram(saveRequest.getDiagram())) {
                    diagramUtil.saveDiagramData(idDTO.getEmail(), saveRequest);

                    return new ResponseEntity<>("Success", HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);
                }
            }
            catch(Exception e) {
                System.out.println(e);
                return new ResponseEntity<>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>("Authentication Error", HttpStatus.UNAUTHORIZED);
    }

    @PutMapping("/update/Diagram")
    public ResponseEntity<String> updateDiagram(@RequestBody saveRequestDTO saveRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()) {
            identificationDTO idDTO = (identificationDTO) authentication.getPrincipal();

            try {
                if(diagramUtil.checkValidDiagram(saveRequest.getDiagram())) {
                    diagramUtil.updateDiagramData(idDTO.getEmail(), saveRequest);

                    return new ResponseEntity<>("Success", HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<>("Fail", HttpStatus.BAD_REQUEST);
                }
            }
            catch(Exception e) {
                System.out.println(e);
                return new ResponseEntity<>("Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>("Authentication Error", HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/search/material")
    public List<materialData> getSearchMaterial(@RequestParam String partial) {
        return diagramUtil.getSearchMaterial(partial);
    }

    @GetMapping("/past/diagram")
    public List<diagramMetaData> getPastDiagram(@RequestParam int year, @RequestParam int month) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()) {
            identificationDTO idDTO = (identificationDTO) authentication.getPrincipal();

            return diagramUtil.getPastDiagram(idDTO.getEmail(), year, month);
        }

        return null;
    }

    @GetMapping("/select/diagram/{diagramId}")
    public ResponseEntity<responseSelectedDiagramDTO> getSelectedDiagram(@PathVariable String diagramId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HttpHeaders headers = new HttpHeaders();

        if(authentication != null && authentication.isAuthenticated()) {
            identificationDTO idDTO = (identificationDTO) authentication.getPrincipal();

            try {
                responseSelectedDiagramDTO diagram = diagramUtil.getSelectedDiagram(idDTO.getEmail(), diagramId);
                headers.setContentType(MediaType.APPLICATION_JSON);

                return new ResponseEntity<>(diagram, headers, HttpStatus.OK);
            }
            catch(Exception e) {
                System.out.println(e);

                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
    }

    @DeleteMapping("/delete/diagram/{diagramId}")
    public ResponseEntity<String> deleteSelectedDiagram(@PathVariable String diagramId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()) {
            identificationDTO idDTO = (identificationDTO) authentication.getPrincipal();

            try {
                diagramUtil.deleteDiagram(diagramId, idDTO.getEmail());

                return new ResponseEntity<>("Success", HttpStatus.OK);
            }
            catch (Exception e) {
                System.out.println(e);

                return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<>("Authentication Error", HttpStatus.UNAUTHORIZED);
    }
}
