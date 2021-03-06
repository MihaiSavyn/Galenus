package ro.sci.gms.web;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import ro.sci.gms.domain.Appointment;
import ro.sci.gms.domain.Patient;
import ro.sci.gms.service.AppointmentService;
import ro.sci.gms.service.ValidationException;
import ro.sci.gms.temp.Li;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentService aptService;

	/**
	 * Just a mocked logged Patient. This is temporary, until Spring Security
	 * integration is done.
	 */
	@Autowired
	private Patient loggedPatient;

	@RequestMapping("")
	public ModelAndView showAllAppointments() throws ValidationException {

		Collection<Appointment> allAppointments = aptService.getAll();

		ModelAndView modelAndView = new ModelAndView("appointments_list");
		modelAndView.addObject("allAppointments", allAppointments);

		Li.st("Showed all appointments.");
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET, params = "action=add")
	public ModelAndView createAppointment() {
		ModelAndView modelAndView = new ModelAndView("appointment_create");

		modelAndView.addObject("appointment", new Appointment());
		return modelAndView;
	}

	@RequestMapping(method = RequestMethod.GET, params = "action=edit")
	public ModelAndView editAppointment(@RequestParam("id") Long id) {
		ModelAndView result = new ModelAndView("appointment_edit");
		Appointment apt = new Appointment();
		if (id != null) {
			apt = aptService.get(id);
		}
		result.addObject("appointment", apt);
		return result;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String saveAppointment(@ModelAttribute Appointment appointment) throws ValidationException {

		Li.st("Saving appointment.");
		 // Should come from FORM or from USER contextual data - logged in USER
		 // Will be removed
		 Date date = new Date();
		 Date time = new Date();
		 date.setDate(15);
		 time.setHours(11);
		 appointment.setPatient(loggedPatient);
		 appointment.setDoctor(loggedPatient.getDoctor());
		 appointment.setDate(date);
		 appointment.setTime(time);
		 // Should come from FORM or from USER contextual data - logged in USER
		
		
		 aptService.save(appointment);
		 System.out.println(appointment.list());

		return "success";
	}

	@RequestMapping(method = RequestMethod.GET, params = "action=delete")
	public String delete(@RequestParam("id") Long id) {
		aptService.delete(id);
		return "success";
	}

	/**
	 * For testing purposes only. 
	 * @return
	 * @throws ValidationException
	 */
	@RequestMapping("/generate")
	public String generate() throws ValidationException {

		aptService.generateSome();

		return "success";
	}
}
