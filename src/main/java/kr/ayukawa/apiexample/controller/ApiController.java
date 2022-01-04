package kr.ayukawa.apiexample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/")
public class ApiController {

	@Autowired
	private RestTemplate restTemplate;

	private Random random = new Random();

	@GetMapping("Today")
	@ResponseBody
	public String showToday() {
		return LocalDateTime.now().toString();
	}

	@GetMapping("Pokemon")
	@ResponseBody
	public Map<String, Object> getRandomPokemon() {
		int pokemonNumber = random.nextInt(151);
		UriComponents uriComponents = UriComponentsBuilder.newInstance()
				.scheme("https")
				.host("pokeapi.co")
				.path("api/v2/pokemon")
				.path("/")
				.path(Integer.toString(pokemonNumber))
				.build(true);
		String url = uriComponents.toUriString();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.107 Safari/537.36");
		HttpEntity<String> httpEntity = new HttpEntity<>("", headers);
		ResponseEntity<Map<String, Object>> returnedEntities =
				this.restTemplate.exchange(
						url,
						HttpMethod.GET,
						httpEntity,
						new ParameterizedTypeReference<Map<String, Object>>(){}
				);
		Map<String, Object> ret = returnedEntities.getBody();
		if(ret.containsKey("abilities")) ret.remove("abilities");
		if(ret.containsKey("forms")) ret.remove("forms");
		if(ret.containsKey("game_indices")) ret.remove("game_indices");
		if(ret.containsKey("held_items")) ret.remove("held_items");
		if(ret.containsKey("location_area_encounters")) ret.remove("location_area_encounters");
		if(ret.containsKey("moves")) ret.remove("moves");
		if(ret.containsKey("species")) ret.remove("species");
		if(ret.containsKey("sprites")) ret.remove("sprites");
		if(ret.containsKey("stats")) ret.remove("stats");

		return ret;
	}
}
