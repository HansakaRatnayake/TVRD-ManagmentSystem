package com.content.security.service;

import com.content.security.dto.VaccinationDTO;
import com.content.security.entity.Vaccination;
import com.content.security.entity.Vaccinationrecord;
import com.content.security.exception.ResourceAlreadyExistException;
import com.content.security.exception.ResourceNotFountException;
import com.content.security.repository.VaccinationRepository;
import com.content.security.util.mapper.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class VaccinationServiceIMPL implements VaccinationService{

    private final ObjectMapper objectMapper;
    private final VaccinationRepository vaccinationRepository;


    @Override
    public List<VaccinationDTO> getAll(HashMap<String, String> params) {

        List<Vaccination> vaccinations = vaccinationRepository.findAll();
        if(!vaccinations.isEmpty()){
            List<VaccinationDTO> vaccinationDTOS = objectMapper.vaccinationListToDtoList(vaccinations);
            if(params.isEmpty()){
                return vaccinationDTOS;
            }else{
                String childrecordid = params.get("childrecordid");
                String clinicid = params.get("clinicid");
                String vaccinationprogressid = params.get("vaccinationprogressid");
                String vaccineofferingid = params.get("vaccineofferingid");

                Stream<VaccinationDTO> stream = vaccinationDTOS.stream();

                if(childrecordid != null) stream = stream.filter(s->s.getChildrecords().getId()==Integer.parseInt(childrecordid));
                if(clinicid != null) stream = stream.filter(s->s.getClinic().getId()==Integer.parseInt(clinicid));
                if(vaccinationprogressid != null) stream = stream.filter(s->s.getVaccinationprogress().getId()==Integer.parseInt(vaccinationprogressid));
                if(vaccineofferingid != null) stream = stream.filter(s->s.getVaccinationrecords().stream().anyMatch(a->a.getVaccineoffering().getId()==Integer.parseInt(vaccineofferingid)));

                return stream.collect(Collectors.toList());
            }
        }else{
            throw new ResourceNotFountException("Vaccinations Not Found!");
        }
    }

    @Override
    public VaccinationDTO create(VaccinationDTO vaccinationDTO) {
        if(vaccinationDTO != null){

            Vaccination vaccination = objectMapper.vaccinationDtoToVaccination(vaccinationDTO);

            if(!vaccinationRepository.existsByChildrecords(vaccination.getChildrecords())){
                for(Vaccinationrecord i : vaccination.getVaccinationrecords()){
                    i.setVaccination(vaccination);
                }

                vaccinationRepository.save(vaccination);
                return vaccinationDTO;
            }else{
                throw new ResourceAlreadyExistException("Vaccination Already Exist!");
            }
        }else{
            throw new ResourceNotFountException("Vaccination Not Found");
        }
    }

    @Override
    public VaccinationDTO update(VaccinationDTO vaccinationDTO) {
        Vaccination vaccinationrec = vaccinationRepository.findById(vaccinationDTO.getId()).orElseThrow(null);

        if(vaccinationrec != null){

            Vaccination vaccination = objectMapper.vaccinationDtoToVaccination(vaccinationDTO);

            if(Objects.equals(vaccinationrec.getClinic().getId(), vaccination.getClinic().getId()) && Objects.equals(vaccinationrec.getChildrecords().getId(), vaccination.getChildrecords().getId())){
                try{
                    vaccination.getVaccinationrecords().forEach(dis -> dis.setVaccination(vaccination));
                    vaccinationRepository.save(vaccination);
                }catch(Exception e){System.out.println(e);}
                return vaccinationDTO;
            } else if(!vaccinationRepository.existsByChildrecords(vaccination.getChildrecords())){
                try{
                    vaccination.getVaccinationrecords().forEach(dis -> dis.setVaccination(vaccination));
                    vaccinationRepository.save(vaccination);
                }catch(Exception e){System.out.println(e);}
                return vaccinationDTO;
            }else{
                throw new ResourceAlreadyExistException("Vaccination Already Exist!");
            }
        }else{
            throw new ResourceNotFountException("Vaccination Not Found");
        }
    }

    @Override
    public String delete(Integer id) {
        Vaccination vaccination = vaccinationRepository.findById(id).orElseThrow(null);

        if(vaccination != null){
            vaccinationRepository.delete(vaccination);
            return "Vaccination Deleted Successfully";
        }else{
            throw new ResourceNotFountException("Vaccination Not Found");
        }
    }
}
