package tfg.urjc.mydoiinfo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import tfg.urjc.mydoiinfo.domain.entities.Article;
import tfg.urjc.mydoiinfo.services.ArticleScrapperService;
import tfg.urjc.mydoiinfo.services.ArticleService;
import tfg.urjc.mydoiinfo.services.ORCIDService;

import java.util.Arrays;
import java.util.List;

@Controller
public class testController {

    @Autowired
    ArticleService articleService;

    @Autowired
    ArticleScrapperService articleScrapperService;

    @Autowired
    ORCIDService orcidService;

    public static final String[] DOIList = {
            //Springer, Elsevier, Science, IEEE, ACM, Conferences
            "https://doi.org/10.1007/978-3-319-78759-6_14","https://doi.org/10.1109/CCGrid.2011.17","https://doi.org/10.1007/978-3-540-88871-0_59",
            //"https://doi.org/10.1134/S0965542519090069", "https://doi.org/10.1134/S0965542512090035", "https://doi.org/10.1007/s12310-021-09471-5",//Springer
            //"https://doi.org/10.1016/j.ecoleng.2014.09.079","https://doi.org/10.1016/j.arabjc.2017.05.011","https://doi.org/10.1016/j.ecoleng.2018.03.006",//ELSEVIER
            //"https://doi.org/10.1126/science.abf1015","https://doi.org/10.1126/science.abb3420","https://doi.org/10.1126/science.abe4943",//"https://doi.org/10.1126/science.370.6523.1384", //Science
            //"https://doi.org/10.1126/sciimmunol.abh2095","https://doi.org/10.1126/sciimmunol.aam6533",//Science Immunology
            //"https://doi.org/10.1109/LCA.2021.3081752","https://doi.org/10.1109/101.8118","https://doi.org/10.1109/JIOT.2020.2983228","https://doi.org/10.1109/JIOT.2020.2984532", //IEEE
            //"https://doi.org/10.1145/3463368","https://doi.org/10.1145/1516533.1516538",
            //"https://dl.acm.org/doi/10.1145/3470005","https://doi.org/10.1145/3488554","https://doi.org/10.1145/3483382.3483388",//ACM
            //"https://doi.org/10.1109/ISCC50000.2020.9219712","https://doi.org/10.1109/INDIN45582.2020.9442143","https://doi.org/10.1145/3372297.3417280", "https://doi.org/10.1145/988772.988777","http://doi.acm.org/10.1145/2661334.2661356"//Conferences
    };

    @RequestMapping("/test")
    public String test(Model model) {
        //"0000-0002-5382-6805","0000-0001-5727-2427","0000-0001-7405-5504"
        //List<Article> articleList = orcidService.getArticlesFromORCIDid("0000-0002-5382-6805");
        List<Article> articleList = articleService.getArticlesFromDOIList(Arrays.asList(DOIList));
        model.addAttribute("articleList",articleList);
        return "table";
    }
}
