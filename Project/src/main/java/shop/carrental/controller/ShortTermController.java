package shop.carrental.controller;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import oracle.sql.DATE;
import shop.carrental.domain.ReserveDTO;
import shop.carrental.domain.ReserveVO;
import shop.carrental.domain.ShortCarVO;
import shop.carrental.service.CarService;
import shop.carrental.service.RentalService;
import shop.carrental.service.ShortTermService;

@Controller
@Log4j
@AllArgsConstructor
@RequestMapping("/short/*")
public class ShortTermController {

	private ShortTermService shortTermService;
	private CarService carService;
	private RentalService rentalService;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/DD");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}

	@GetMapping("/")
	public String basic() {
		log.info("short");
		return "/short/list";
	}

	@GetMapping("/list")
	public void list(Model model) {
		log.info("list");
		shortTermService.list(model);
	}

	/* shortTerm_inland */
	@GetMapping("/listInland")
	public void listInland(ReserveVO vo, Model model) {

		log.info("shortTerm");
		model.addAttribute("branchList", shortTermService.listBranch());
		model.addAttribute("result", shortTermService.getReservationInfo(vo));
	}

	@PostMapping("/listInland")
	public String listInland(ReserveVO vo, RedirectAttributes rttr) {

		log.info("shortTerm:" + vo);
		shortTermService.listInland(vo);
		rttr.addAttribute("result", vo.getReserve_seq());
		return "redirect:/short/detail";
	}

	/* shortTerm_jeju */
	@GetMapping("/listJeju")
	public void listJeju(ReserveVO vo, Model model) {

		log.info("listJeju");
		model.addAttribute("result", shortTermService.getReservationInfo(vo));
	}

	@PostMapping("/listJeju")
	public String listJeju(ReserveVO vo, RedirectAttributes rttr) {

		log.info("listJeju: " + vo);
		shortTermService.listJeju(vo);
		rttr.addAttribute("result", vo.getReserve_seq());
		return "redirect:/short/detail";
	}

	/* detail */
	@GetMapping("/detail")
	public void detail(ReserveVO vo, Model model) {
		/* ChronoUnit chronoUnit = */
		log.info("::::::::::::::::: " + vo);
		model.addAttribute("car", carService.getDetailCar(vo.getSc_seq()));
		model.addAttribute("insuranceList", shortTermService.listInsurance());
		log.info("detail");
	}

	@PostMapping("/detail")
	public String detail(ReserveDTO dto, RedirectAttributes rttr) {

		return "redirect:/short/payComplete";
	}

	
	/* payComplete */
	 
	 @GetMapping("/payComplete") 
	 public void payComplete(ReserveVO vo, Model model) {
		 model.addAttribute("ReservationInfo",shortTermService.getReservationInfo(vo)); 
	 log.info("payComplete"); 
	 }
	

	@PostMapping("/payComplete")
	public String payComplete(ReserveDTO dto, RedirectAttributes rttr) {
		log.info("payComplete");

		shortTermService.registerReservation(dto);

		rttr.addAttribute("seq", dto.getReserve_seq());
		return "redirect:/short/reserve";
	}

	/*reserve */
	@GetMapping("/reserve")
	public void reserve(ReserveVO vo, Model model) {
		model.addAttribute("ReservationInfo", shortTermService.getReservationInfo(vo));
		log.info("reserve");
	}

	@PostMapping("/reserve")
	public String reserve(ReserveDTO dto, RedirectAttributes rttr) {
		log.info("reserve");

		shortTermService.registerReservation(dto);

		rttr.addAttribute("seq", dto.getReserve_seq());
		return "redirect:/short/reserve";
	}

	/*
	 * @GetMapping("/carInfo") public ResponseEntity<CarVO>
	 * getCarInfo(@RequestParam("segment_seq") int seq){
	 * log.info("segment_seq:"+seq);
	 * log.info("carInfo::::::::::"+shortTermService.getCarInfo(1));
	 * if(shortTermService.getCarInfo(seq)!=null) return new
	 * ResponseEntity<CarVO>(shortTermService.getCarInfo(1),HttpStatus.OK); else
	 * return new ResponseEntity<CarVO>(new
	 * CarVO(),HttpStatus.INTERNAL_SERVER_ERROR);
	 * 
	 * }
	 */
	/*
	 * @PostMapping("/shortTerm_payment2") public String shortTerm_payment2post(vo)
	 * { log.info(); }
	 */

	/*
	 * @PostMapping("/register") public ResponseEntity<ReservationDTO>
	 * register(@RequestBody ReservationDTO dto){ log.info(dto); return new
	 * ResponseEntity<ReservationDTO>(dto, HttpStatus.OK);
	 * 
	 * }
	 */
}