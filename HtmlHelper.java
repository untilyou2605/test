/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author sonnt
 */
public class HtmlHelper {
    public static String hyperlink(String label, String href)
    {
        return "<a class=\"page_hyperlink\" href=\""+href+"\">" + label + "</a>"; 
    }
    
    public static String pager(int pageindex,int pagecount, int gap)
    {
        String result = "";
        if(pageindex > gap)
            result += hyperlink("First","manageQuiz?page="+1);
        
        for (int i = gap-1; i > 0; i--) {
            if(pageindex - gap >= 0)
                result += hyperlink(""+(pageindex - i),"manageQuiz?page="+(pageindex - i));
            
        }
        
        result += "<span class=\"page_pageindex\">"+pageindex+"</span>";
        
        for (int i = 1; i <= gap; i++) {
            if(pageindex + i <= pagecount)
                result += hyperlink(""+(pageindex + i),"manageQuiz?page="+(pageindex + i));
          
        }
        
        if(pageindex + gap < pagecount)
            result += hyperlink("Last","manageQuiz?page="+pagecount);
        
        return result;
    }
}
