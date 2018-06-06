package com.arp.utility;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.arp.service.UploadDwnService;

@ComponentScan(basePackages = "com.arp.service")
@RestController
public class Controller {

	@Autowired
	private SharingPortalRepository sharingportalRepository;

	SharingPortal sharingportal = new SharingPortal();

	@Autowired
	UploadDwnService service;

	@RequestMapping(value = "/searchId/{id}", method = RequestMethod.GET)
	public SharingPortal searchId(@PathVariable("id") String id) {
		long start = System.currentTimeMillis();
		SharingPortal shr = sharingportalRepository.findById(id);
		long end = System.currentTimeMillis();
		System.out.println("save time for search id " + (end - start));
		return shr;
	}

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public List<SharingPortal> listAll() {
		long start = System.currentTimeMillis();
		List<SharingPortal> shr = sharingportalRepository.showList();
		long end = System.currentTimeMillis();
		System.out.println("save time for list all " + (end - start));
		return shr;
	}

	@RequestMapping(value = "/topThree", method = RequestMethod.GET)
	public List<SharingPortal> listTop() {
		List<SharingPortal> sharingportal = sharingportalRepository.showList();
		Collections.sort(sharingportal, new MyCountComp());
		List<SharingPortal> topsharingportal = sharingportal.subList(0, 3);
		for (SharingPortal e : topsharingportal) {
			System.out.println(e.toString());
		}
		return topsharingportal;
	}

	@RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
	public @ResponseBody String uploadFiles(@RequestParam("file") MultipartFile file,
			@RequestParam("filename") String filename, @RequestParam("fileDesc") String fileDesc,
			@RequestParam("readMe") String readMe, @RequestParam("anthemId") String anthemId,
			@RequestParam("email") String email, @RequestParam("category") String category) {
		long start = System.currentTimeMillis();
		SharingPortal sharingportal = service.uploadForm(file, filename, fileDesc, readMe, anthemId, email, category);
		System.out.println("upload-->" + sharingportal.getFile().length());
		sharingportalRepository.save(sharingportal);
		long end = System.currentTimeMillis();
		System.out.println("save time for upload " + (end - start));
		return "success";
	}

	@RequestMapping(value = "/download/{id}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> download(@PathVariable("id") String id) throws IOException {
		long start = System.currentTimeMillis();
		SharingPortal sharingportal = sharingportalRepository.findById(id);
		long end2 = System.currentTimeMillis();
		System.out.println("save time for download after search by id" + (end2 - start));
		int count = sharingportal.getDwnCount() + 1;
		System.out.println("count set before db");
		sharingportal.setDwnCount(count);
		// System.out.println("download-->" + sharingportal);
		long end = System.currentTimeMillis();
		System.out.println("save time for download before DB change" + (end - start));
		sharingportalRepository.save(sharingportal);
		ResponseEntity<byte[]> dwn = service.downloadForm(sharingportal.file, sharingportal.fileName);
		long end1 = System.currentTimeMillis();
		System.out.println("save time for full download " + (end1 - start));
		return dwn;
	}

	@RequestMapping(value = "/deleteAll", method = RequestMethod.GET)
	public String deleteAll() {
		sharingportalRepository.deleteAll();
		return "success";
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteId(@PathVariable("id") String id) {
		sharingportalRepository.deleteById(id);
		return "success";
	}

}
