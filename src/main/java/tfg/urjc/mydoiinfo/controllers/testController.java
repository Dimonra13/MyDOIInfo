package tfg.urjc.mydoiinfo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.urjc.mydoiinfo.services.ArticleScrapperService;

import java.util.Arrays;

@Controller
public class testController {

    @Autowired
    ArticleScrapperService articleScrapperService;

    public static final String[] DOIList = {
            //IEEE, ACM, Science, SPRINGER, ELSEVIER
            //"https://doi.org/10.1126/science.abf1015","https://doi.org/10.1126/science.abb3420", "https://doi.org/10.1126/science.abj3624",//"https://doi.org/10.1126/science.abe4943", //Science
            "https://doi.org/10.1109/LCA.2021.3081752","https://doi.org/10.1109/LCA.2020.2973991" //,"https://doi.org/10.1109/101.8118","https://doi.org/10.1109/JIOT.2020.2983228","https://doi.org/10.1109/JIOT.2020.2984532", "https://doi.org/10.1109/I-SMAC49090.2020.9243527", //IEEE
            //"https://dl.acm.org/doi/10.1145/3470005","https://doi.org/10.1145/3488554","https://doi.org/10.1145/3483382.3483388"//,"https://doi.org/10.1145/3483382.3483384" //,"https://dl.acm.org/doi/10.1145/3476415.3476417" //,"https://dl.acm.org/doi/10.1145/1067268.1067287" //ACM
    };

    @RequestMapping("/")
    public void test() {
        System.out.println(articleScrapperService.getArticleInfoFromDOIList(Arrays.asList(DOIList)).toString());
    }
}
