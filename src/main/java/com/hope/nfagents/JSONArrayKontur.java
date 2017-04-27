/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hope.nfagents;

import com.vaadin.ui.Notification;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Orel
 */
public class JSONArrayKontur {

    private JSONArray json;
    private static final String jKey="3208d29d15c507395db770d0e65f3711e40374df";
    private static final String jInn="6663003127";
    
    private String inn, ogrn, 
            kppUL, okpoUL, okatoUL, okfsUL, oktmoUL, okoguUL, okopfUL, opfUL,
            legalNameShortUL, legalNameFullUL, legalNameDateUL,
            legalAddressUL,
            statusStringUL, statusReorganizingUL, statusDissolvingUL, statusDissolvedUL, statusDateUL,
            registrationDateUL,
            dissolutionDateUL,
            fioIP, okpoIP, okatoIP, okfsIP, okoguIP, okopfIP, opfIP, oktmoIP, registrationDateIP, dissolutionDateIP,
            statusStringStatusIP,dissolvedStatusIP,dateStatusIP;

    
    public JSONArrayKontur(String key, String inn) {
       json = new JSONArray();
       String url;
       if ((key!=null)&&(!"".equals(key))&&(inn!=null)&&(!"".equals(inn)))
          url="https://focus-api.kontur.ru/api3/req?key="+key+"&inn="+inn;
       else
          url="https://focus-api.kontur.ru/api3/req?key="+jKey+"&inn="+jInn;
        try {
            json=MyJSONUtils.readJsonFromUrl(url);
        } catch (IOException ex) {
            Notification.show(ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            return;
        }
       
        if (json.length()==0) return;
        
        JSONObject obj = (JSONObject) json.get(0);

        try {
            this.inn=obj.getString("inn");
            ogrn=obj.getString("ogrn");
        } catch (JSONException ee) {
        }
        try {
            JSONObject ul = (JSONObject) obj.get("UL");
            //КПП
            kppUL=MyJSONUtils.getValue(ul,"kpp");
            //Код ОКПО - okpo,
            okpoUL=MyJSONUtils.getValue(ul,"okpo");
            //Код ОКАТО - okato,
            okatoUL=MyJSONUtils.getValue(ul,"okato");
            //Код ОКФС - okfs,
            okfsUL=MyJSONUtils.getValue(ul,"okfs");
            //Код ОКТМО - oktmo,
            oktmoUL=MyJSONUtils.getValue(ul,"oktmo");
            //Код ОКОГУ - okogu,
            okoguUL=MyJSONUtils.getValue(ul,"okogu");
            //Код ОКОПФ - okopf,
            okopfUL=MyJSONUtils.getValue(ul,"okopf");
            //Наименование организационно-правовой формы - opf,
            opfUL=MyJSONUtils.getValue(ul,"opf");

            try {
                // Наименование юридического лица - legalName:
                JSONObject uln = (JSONObject) ul.get("legalName");
                // Краткое наименование организации - short,
                legalNameShortUL = MyJSONUtils.getValue(uln,"short");
                // Полное наименование организации - full,
                legalNameFullUL = MyJSONUtils.getValue(uln,"full");
                // Дата - date
                legalNameDateUL = MyJSONUtils.getValue(uln,"date");
            } catch (JSONException ee) {
            }
            
            legalAddressUL="";
            try {
                // Юридический адрес - legalAddress:
                JSONObject ulla = (JSONObject) ul.get("legalAddress");
                // Разобранный на составляющие адрес в РФ - parsedAddressRF: 
                JSONObject ullap = (JSONObject) ulla.get("parsedAddressRF");
                String a1="";
                a1=MyJSONUtils.getValue(ullap,"zipCode");
                if ((a1!=null)&&(!"".equals(a1))) legalAddressUL=a1+", ";
                try{
                // Регион - regionName:
                JSONObject ullapr = (JSONObject) ullap.get("regionName");
                a1=MyJSONUtils.getValue(ullapr,"topoValue");
                if ((a1!=null)&&(!"".equals(a1))) legalAddressUL=legalAddressUL+a1+" "+MyJSONUtils.getValue(ullapr,"topoShortName")+", ";
                }catch(JSONException je){}
                try{
                // Район - district: 
                JSONObject ullapr = (JSONObject) ullap.get("district");
                a1=MyJSONUtils.getValue(ullapr,"topoValue");
                if ((a1!=null)&&(!"".equals(a1))) legalAddressUL=legalAddressUL+a1+" "+MyJSONUtils.getValue(ullapr,"topoShortName")+", ";
                }catch(JSONException je){}
                try{
                // Город - city:
                JSONObject ullapr = (JSONObject) ullap.get("city");
                a1=MyJSONUtils.getValue(ullapr,"topoValue");
                if ((a1!=null)&&(!"".equals(a1))) legalAddressUL=legalAddressUL+MyJSONUtils.getValue(ullapr,"topoShortName")+" "+a1+", ";
                }catch(JSONException je){}
                try{
                // Населенный пункт - settlement:
                JSONObject ullapr = (JSONObject) ullap.get("settlement");
                a1=MyJSONUtils.getValue(ullapr,"topoValue");
                if ((a1!=null)&&(!"".equals(a1))) legalAddressUL=legalAddressUL+MyJSONUtils.getValue(ullapr,"topoShortName")+" "+a1+", ";
                }catch(JSONException je){}
                try{
                // Улица - street:
                JSONObject ullapr = (JSONObject) ullap.get("settlement");
                a1=MyJSONUtils.getValue(ullapr,"topoValue");
                if ((a1!=null)&&(!"".equals(a1))) legalAddressUL=legalAddressUL+MyJSONUtils.getValue(ullapr,"topoShortName")+" "+a1+", ";
                }catch(JSONException je){}
                try{
                // Дом - house:
                JSONObject ullapr = (JSONObject) ullap.get("house");
                a1=MyJSONUtils.getValue(ullapr,"topoValue");
                if ((a1!=null)&&(!"".equals(a1))) legalAddressUL=legalAddressUL+MyJSONUtils.getValue(ullapr,"topoShortName")+" "+a1+", ";
                }catch(JSONException je){}
                try{
                // Корпус - bulk:
                JSONObject ullapr = (JSONObject) ullap.get("bulk");
                a1=MyJSONUtils.getValue(ullapr,"topoValue");
                if ((a1!=null)&&(!"".equals(a1))) legalAddressUL=legalAddressUL+MyJSONUtils.getValue(ullapr,"topoShortName")+" "+a1+", ";
                }catch(JSONException je){}
                try{
                // Офис/квартира/комната - flat:
                JSONObject ullapr = (JSONObject) ullap.get("flat");
                a1=MyJSONUtils.getValue(ullapr,"topoValue");
                if ((a1!=null)&&(!"".equals(a1))) legalAddressUL=legalAddressUL+MyJSONUtils.getValue(ullapr,"topoShortName")+" "+a1+", ";
                }catch(JSONException je){}
            } catch (JSONException ee) {
                legalAddressUL="";
            }
            
            try {
                // Статус организации - status: 
                JSONObject uls = (JSONObject) ul.get("status");
                // Неформализованное описание статуса - statusString                
                statusStringUL=MyJSONUtils.getValue(uls,"statusString");
                // В процессе реорганизации (может прекратить деятельность в результате реорганизации) - reorganizing,
                statusReorganizingUL=MyJSONUtils.getValue(uls,"reorganizing");
                // В стадии ликвидации (либо планируется исключение из ЕГРЮЛ) - dissolving,
                statusDissolvingUL=MyJSONUtils.getValue(uls,"dissolving");
                // Недействующее - dissolved,
                statusDissolvedUL=MyJSONUtils.getValue(uls,"dissolved");
                // Дата - date
                statusDateUL=MyJSONUtils.getValue(uls,"date");
            } catch (JSONException jstat) {                
            }
            
            // Дата образования - registrationDate,
            registrationDateUL=MyJSONUtils.getValue(ul,"registrationDate");
            // Дата прекращения деятельности в результате ликвидации, реорганизации или других событий - dissolutionDate
            dissolutionDateUL=MyJSONUtils.getValue(ul,"dissolutionDate");
            
        } catch (JSONException jsee) {
        }

        try {
            // Информация об индивидуальном предпринимателе - IP:
            JSONObject ip = (JSONObject) obj.get("IP");
            // ФИО - fio,
            fioIP=MyJSONUtils.getValue(ip,"fio");
            // ОКПО - okpo,
            okpoIP=MyJSONUtils.getValue(ip,"okpo");
            // ОКАТО - okato,
            okatoIP=MyJSONUtils.getValue(ip,"okato");
            // ОКФС - okfs,
            okfsIP=MyJSONUtils.getValue(ip,"okfs");
            // ОКОГУ - okogu,
            okoguIP=MyJSONUtils.getValue(ip,"okogu");
            // Код ОКОПФ - okopf,
            okopfIP=MyJSONUtils.getValue(ip,"okopf");
            // Наименование организационно-правовой формы - opf,
            opfIP=MyJSONUtils.getValue(ip,"opf");
            // ОКТМО - oktmo,
            oktmoIP=MyJSONUtils.getValue(ip,"oktmo");
            // Дата образования - registrationDate,
            registrationDateIP=MyJSONUtils.getValue(ip,"registrationDate");
            // Дата прекращения деятельности - dissolutionDate,
            dissolutionDateIP=MyJSONUtils.getValue(ip,"dissolutionDate");
            
            try {
                // Статус ИП - status: {
                JSONObject uls = (JSONObject) ip.get("status");
                // Неформализованное описание статуса - statusString,
                statusStringStatusIP=MyJSONUtils.getValue(uls,"statusString");
                // Недействующий - dissolved,
                dissolvedStatusIP=MyJSONUtils.getValue(uls,"dissolved");
                //Дата - date
                dateStatusIP=MyJSONUtils.getValue(uls,"date");
            } catch (JSONException ee){                
            }
            
        } catch (JSONException e) {
        }
        
    }
   
   
   /*
   
ИНН(ИП) - inn,
ОГРН(ИП) - ogrn,
Ссылка на карточку юридического лица (ИП) в Контур.Фокусе (для работы требуется подписка на Контур.Фокус и дополнительная авторизация) - focusHref,
Информация о юридическом лице - UL: {
КПП - kpp,
Код ОКПО - okpo,
Код ОКАТО - okato,
Код ОКФС - okfs,
Код ОКТМО - oktmo,
Код ОКОГУ - okogu,
Код ОКОПФ - okopf,
Наименование организационно-правовой формы - opf,
Наименование юридического лица - legalName: {
Краткое наименование организации - short,
Полное наименование организации - full,
Дата - date
},
Юридический адрес - legalAddress: {
Разобранный на составляющие адрес в РФ - parsedAddressRF: {
Индекс - zipCode,
Код региона - regionCode,
Регион - regionName: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Район - district: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Город - city: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Населенный пункт - settlement: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Улица - street: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Дом - house: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Корпус - bulk: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Офис/квартира/комната - flat: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Код КЛАДР (ФИАС) - kladrCode
},
Дата - date
},
Филиалы и представительства - branches: [
{
Наименование филиала или представительства - name,
Разобранный на составляющие адрес в РФ - parsedAddressRF: {
Индекс - zipCode,
Код региона - regionCode,
Регион - regionName: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Район - district: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Город - city: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Населенный пункт - settlement: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Улица - street: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Дом - house: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Корпус - bulk: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Офис/квартира/комната - flat: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Код КЛАДР (ФИАС) - kladrCode
},
Адрес вне РФ - foreignAddress: {
Наименование страны - countryName,
Строка, содержащая адрес - addressString
},
Дата - date
}
],
Статус организации - status: {
Неформализованное описание статуса - statusString,
В процессе реорганизации (может прекратить деятельность в результате реорганизации) - reorganizing,
В стадии ликвидации (либо планируется исключение из ЕГРЮЛ) - dissolving,
Недействующее - dissolved,
Дата - date
},
Дата образования - registrationDate,
Дата прекращения деятельности в результате ликвидации, реорганизации или других событий - dissolutionDate,
Лица, имеющие право подписи без доверенности (руководители) - heads: [
{
ФИО - fio,
ИННФЛ - innfl,
Должность - position,
Дата - date
}
],
Управляющие компании - managementCompanies: [
{
Наименование управляющей организации - name,
ИНН управляющей организации (если указан) - inn,
ОГРН управляющей организации (если указан) - ogrn,
Дата - date
}
],
history: {
КПП - kpps: [
{
КПП - kpp,
Дата - date
}
],
Наименование юридического лица - legalNames: [
{
Краткое наименование организации - short,
Полное наименование организации - full,
Дата - date
}
],
Список юридических адресов из истории - legalAddresses: [
{
Разобранный на составляющие адрес в РФ - parsedAddressRF: {
Индекс - zipCode,
Код региона - regionCode,
Регион - regionName: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Район - district: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Город - city: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Населенный пункт - settlement: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Улица - street: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Дом - house: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Корпус - bulk: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Офис/квартира/комната - flat: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Код КЛАДР (ФИАС) - kladrCode
},
Дата - date
}
],
Филиалы и представительства из истории - branches: [
{
Наименование филиала или представительства - name,
Разобранный на составляющие адрес в РФ - parsedAddressRF: {
Индекс - zipCode,
Код региона - regionCode,
Регион - regionName: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Район - district: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Город - city: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Населенный пункт - settlement: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Улица - street: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Дом - house: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Корпус - bulk: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Офис/квартира/комната - flat: {
Краткое наименование вида топонима - topoShortName,
Полное наименование вида топонима - topoFullName,
Значение топонима - topoValue
},
Код КЛАДР (ФИАС) - kladrCode
},
Адрес вне РФ - foreignAddress: {
Наименование страны - countryName,
Строка, содержащая адрес - addressString
},
Дата - date
}
],
Управляющие компании - managementCompanies: [
{
Наименование управляющей организации - name,
ИНН управляющей организации (если указан) - inn,
ОГРН управляющей организации (если указан) - ogrn,
Дата - date
}
],
Лица, имеющие право подписи без доверенности (руководители) из истории - heads: [
{
ФИО - fio,
ИННФЛ - innfl,
Должность - position,
Дата - date
}
]
}
},
Информация об индивидуальном предпринимателе - IP: {
ФИО - fio,
ОКПО - okpo,
ОКАТО - okato,
ОКФС - okfs,
ОКОГУ - okogu,
Код ОКОПФ - okopf,
Наименование организационно-правовой формы - opf,
ОКТМО - oktmo,
Дата образования - registrationDate,
Дата прекращения деятельности - dissolutionDate,
Статус ИП - status: {
Неформализованное описание статуса - statusString,
Недействующий - dissolved,
Дата - date
}
},
Экспресс-отчет по контрагенту - briefReport: {
Сводная информация из экспресс-отчета - summary: {
Наличие информации, помеченной зеленым цветом - greenStatements,
Наличие информации, помеченной желтым цветом - yellowStatements,
Наличие информации, помеченной красным цветом - redStatements
},
href
},
Контактные телефоны из Контур.Справочника (для получения контактов требуется отдельная подписка и вызов отдельного метода) - contactPhones: {
Количество найденных контактых телефонов - count,
phones: [
]
}
}
]   
*/   

    public String getInn() {
        return inn;
    }

    public String getOgrn() {
        return ogrn;
    }

    public String getKppUL() {
        return kppUL;
    }

    public String getOkpoUL() {
        return okpoUL;
    }

    public String getOkatoUL() {
        return okatoUL;
    }

    public String getOkfsUL() {
        return okfsUL;
    }

    public String getOktmoUL() {
        return oktmoUL;
    }

    public String getOkoguUL() {
        return okoguUL;
    }

    public String getOkopfUL() {
        return okopfUL;
    }

    public String getOpfUL() {
        return opfUL;
    }

    public String getLegalNameShortUL() {
        return legalNameShortUL;
    }

    public String getLegalNameFullUL() {
        return legalNameFullUL;
    }

    public String getLegalNameDateUL() {
        return legalNameDateUL;
    }

    public String getLegalAddressUL() {
        return legalAddressUL;
    }

    public String getStatusStringUL() {
        return statusStringUL;
    }

    public String getStatusReorganizingUL() {
        return statusReorganizingUL;
    }

    public String getStatusDissolvingUL() {
        return statusDissolvingUL;
    }

    public String getStatusDissolvedUL() {
        return statusDissolvedUL;
    }

    public String getStatusDateUL() {
        return statusDateUL;
    }

    public String getRegistrationDateUL() {
        return registrationDateUL;
    }

    public String getDissolutionDateUL() {
        return dissolutionDateUL;
    }

    public String getFioIP() {
        return fioIP;
    }

    public String getOkpoIP() {
        return okpoIP;
    }

    public String getOkatoIP() {
        return okatoIP;
    }

    public String getOkfsIP() {
        return okfsIP;
    }

    public String getOkoguIP() {
        return okoguIP;
    }

    public String getOkopfIP() {
        return okopfIP;
    }

    public String getOpfIP() {
        return opfIP;
    }

    public String getOktmoIP() {
        return oktmoIP;
    }

    public String getRegistrationDateIP() {
        return registrationDateIP;
    }

    public String getDissolutionDateIP() {
        return dissolutionDateIP;
    }

    public String getStatusStringStatusIP() {
        return statusStringStatusIP;
    }

    public String getDissolvedStatusIP() {
        return dissolvedStatusIP;
    }

    public String getDateStatusIP() {
        return dateStatusIP;
    }

   
}
