package com.hope.nfagents;

import com.fasterxml.jackson.databind.ObjectMapper;
import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.WebBrowser;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.renderers.HtmlRenderer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

import org.json.JSONArray;
import org.json.JSONException;
/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of a html page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {

    final VerticalLayout layout = new VerticalLayout();
    final HorizontalLayout loBtns = new HorizontalLayout();
    final TextField inn = new TextField("ИНН организации:");
    Image captcha = new Image();
    TextField captchaValue = new TextField("Введите текст, указанный на картинке:");
    String captchaTokenValue="";
    Grid<MyItem> table = new Grid<>();
    
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final WebBrowser webBrowser = Page.getCurrent().getWebBrowser();
        String IP = webBrowser.getAddress();
        VaadinService.getCurrentRequest().getWrappedSession().setAttribute("client IP", IP);
        
        layout.setMargin(true);
        layout.setSpacing(true);
        loBtns.setMargin(true);
        loBtns.setSpacing(true);
        
        Button button = new Button("Проверить nalog.ru");
        button.addClickListener( e -> {
            checkNalogRU();
        });

        Button button2 = new Button("Проверить kontur.ru");
        button2.addClickListener( e -> {
            checkKonturRU();
        });
        
        loBtns.addComponents(button, button2);
        layout.addComponents(inn, captcha, captchaValue, loBtns, table);
        setContent(layout);
        table.setSizeFull();
       
        table.addColumn(MyItem::getInn).setCaption("ИНН");
        table.addColumn(MyItem::getKpp).setCaption("КПП");
        table.addColumn(MyItem::getOgrn).setCaption("ОГРН");
        table.addColumn(MyItem::getName, new HtmlRenderer()).setCaption("Наименование");
        table.addColumn(MyItem::getAddress).setCaption("Адрес");
        table.addColumn(MyItem::getDateReg).setCaption("Дата регистрации");
        
        table.setSizeFull();
        layout.setExpandRatio(table, 1);

        getCaptcha();
    }

    private void getCaptcha() {
        try {
            HttpClient hc=new HttpClient();
            GetMethod gm = new GetMethod("https://egrul.nalog.ru/static/captcha.html");
            int sta = hc.executeMethod(gm);
            captchaTokenValue = gm.getResponseBodyAsString();
            captchaValue.clear();
            
            String urlStr = "https://egrul.nalog.ru/static/captcha.html?a="+captchaTokenValue;
            captcha.setSource(new ExternalResource(urlStr));
        } catch (IOException ex) {
                Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
        }
    }
    

    private void checkNalogRU() {

        List<MyItem> orgList = new ArrayList<>();
        
        
        HttpClient httpclient = new HttpClient();
        PostMethod httpPost = new PostMethod("https://egrul.nalog.ru/");
        httpPost.addParameter("kind", "");
        httpPost.addParameter("srchUl", "ogrn");
        httpPost.addParameter("ogrninnul", inn.getValue());
        httpPost.addParameter("namul", "");
        httpPost.addParameter("regionul", "");
        httpPost.addParameter("srchFl", "ogrn");
        httpPost.addParameter("ogrninnfl", "");
        httpPost.addParameter("fam", "");
        httpPost.addParameter("nam", "");
        httpPost.addParameter("otch", "");
        httpPost.addParameter("region", "");
        httpPost.addParameter("captcha", captchaValue.getValue());
        httpPost.addParameter("captchaToken", captchaTokenValue);

        try {

            httpclient.executeMethod(httpPost);

            BufferedReader br = new BufferedReader(new InputStreamReader(httpPost.getResponseBodyAsStream(), "UTF8"));
            String line;
            while ((line = br.readLine()) != null) {

                ObjectMapper mapper = new ObjectMapper();
                HashMap map = mapper.readValue(line, HashMap.class);
                //System.out.println(map.toString());
                HashMap err = (HashMap) map.get("ERRORS");
                if (err != null) {
                    String error = "";
                    for (Object v : err.values()) {
                        ArrayList<HashMap> ar = (ArrayList<HashMap>) v;
                        error += ar.toString();
                    }
                    Notification.show(error, Notification.Type.ERROR_MESSAGE);
                    //getCaptcha();
                    return;
                }
                ArrayList<HashMap> ar = (ArrayList<HashMap>) map.get("rows");
                //HashMap<String, String> req = new HashMap<String, String>();

                MyItem mi = new MyItem();

                for (int i = 0; i < ar.size(); i++) {

                    if (ar.get(i).containsKey("INN")) {
                        mi.setInn(ar.get(i).get("INN").toString());
                    }

                    if (ar.get(i).containsKey("KPP")) {
                        mi.setKpp(ar.get(i).get("KPP").toString());
                    }

                    if (ar.get(i).containsKey("OGRN")) {
                        mi.setOgrn(ar.get(i).get("OGRN").toString());
                    }

                    if (ar.get(i).containsKey("NAME")) {
                        mi.setName(ar.get(i).get("NAME").toString());
                    }

                    if (ar.get(i).containsKey("ADRESTEXT")) {
                        mi.setAddress(ar.get(i).get("ADRESTEXT").toString());
                    }

                    if (ar.get(i).containsKey("DTREG")) {
                        mi.setDateReg(ar.get(i).get("DTREG").toString());
                    }
                    
                    orgList.add(mi);
                    
                }

            }

            table.setItems(orgList);

        } catch (IOException ex) {
                Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
        } finally {
            httpPost.releaseConnection();
        }
    }
    

    private void checkKonturRU() {

        List<MyItem> orgList = new ArrayList<>();
        
        try {
            /*
            URL myservice = new URL(url);
            InputStream openStream = myservice.openStream();            
            BufferedReader br = new BufferedReader(new InputStreamReader(openStream, "UTF8"));
            String line;
            String json="";
            while ((line = br.readLine()) != null) {
//                if (line.contains(","))
                    json=json+line;
            }

            */
            JSONArrayKontur ar = new JSONArrayKontur("","");
            
//            JSONObject obj = (JSONObject)json.get(1);
//            System.out.println(obj.get("inn"));

            

            
       /*     
        JSONObject obj = new JSONObject(json);
        JSONArray ar = obj.getJSONArray("UL");
       */
        MyItem mi = new MyItem();

        mi.setInn(ar.getInn());
        mi.setKpp(ar.getKppUL());
        mi.setOgrn(ar.getOgrn());
        mi.setName(ar.getLegalNameFullUL());
        mi.setAddress(ar.getLegalAddressUL());
        mi.setDateReg(ar.getRegistrationDateUL());
        
        if (!mi.isClear()) orgList.add(mi);

/*        
                for (int i = 0; i < ar.length(); i++) {
                    
                    mi.clear();
                    JSONObject obj = (JSONObject)ar.get(i);
                    
                    try {
                        mi.setInn(obj.getString("inn"));
                        mi.setOgrn(obj.getString("ogrn"));
                    }catch(JSONException jse){                        
                    }
                    try {
                        JSONObject ul = (JSONObject) obj.get("UL");
                        mi.setKpp(ul.getString("kpp"));
                        
                        JSONObject uln = (JSONObject) ul.get("legalName");
                        mi.setName(uln.getString("full"));
                    }catch(JSONException jse){                        
                    }

//                    if (ar.getString(i).contains("ogrn")) {
//                        mi.setOgrn(ar.getString(i));
//                    }
//
//                    if (ar.getString(i).contains("legalName")) {
//                        mi.setName(ar.getString(i));
//                    }
//
//                    if (ar.getString(i).contains("legalAddress")) {
//                        mi.setAddress(ar.getString(i));
//                    }
//
//                    if (ar.getString(i).contains("date")) {
//                        mi.setDateReg(ar.getString(i));
//                    }
                    
                    if (!mi.isClear()) orgList.add(mi);
                    
                }

*/

            table.setItems(orgList);

        } catch (Exception ex) {
                Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
        } finally {
            
        }
    }

   
    
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
