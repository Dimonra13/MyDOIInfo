package tfg.urjc.mydoiinfo.controllers.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/search")
public class SearchController {

    @RequestMapping("/doi")
    public String getSearchDOIPage(Model model, @RequestParam(value = "inputDOI", required = false) String inputDOI) {
        model.addAttribute("inputDOI",inputDOI);
        return "search/search_by_doi";
    }
}
