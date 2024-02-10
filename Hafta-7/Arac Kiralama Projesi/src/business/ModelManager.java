package business;

import core.Helper;
import dao.ModelDao;
import entity.Model;

import javax.swing.text.html.HTMLEditorKit;
import java.util.ArrayList;
public class ModelManager {

    private final ModelDao modelDao = new ModelDao();

    public Model getById(int id) {
        return this.modelDao.getById(id);
    }

    public ArrayList<Model> findAll() {
        return this.modelDao.findAll();
    }

    public ArrayList<Object[]> getForTable(int size, ArrayList<Model> modelList) {
        ArrayList<Object[]> modelObjList = new ArrayList<>();
        for (Model obj : modelList) {
            int i = 0;
            Object[] rowObject = new Object[size];
            rowObject[i++] = obj.getId();
            rowObject[i++] = obj.getBrand().getName(); //model classında Brand tanımlamıstım.
            rowObject[i++] = obj.getName();
            rowObject[i++] = obj.getType();
            rowObject[i++] = obj.getYear();
            rowObject[i++] = obj.getFuel();
            rowObject[i++] = obj.getGear();
            modelObjList.add(rowObject);
        }
        return modelObjList;
    }

    public boolean save (Model model) {
        if (this.getById(model.getId()) != null) {
            Helper.showMsg("error");
            return false;
        }
        return this.modelDao.save(model);
    }

    public boolean update (Model model) {
        if (this.getById(model.getId()) == null){
            Helper.showMsg(model.getId() + " ID kayıtlı model bulunamadı");
            return false;
        }
        return this.modelDao.update(model);
    }

    public boolean delete (int id) {
        if (this.getById(id) == null){
            Helper.showMsg(id + " ID kayıtlı model bulunamadı");
            return false;
        }
        return this.modelDao.delete(id);
    }
//*****************************************************************************************************************

    public ArrayList<Model> getByListBrandId(int brandId) { //
        return this.modelDao.getByListBrandId(brandId); //buradan brandmanager a gidiyoruz ve
        //sil action a sadece marka silmesine ek olarak model deki verileri de sil kodunu ekliyoruz
    }


//*****************************************************************************************************************
        //AKILLI KOD İLE SSEARCH İŞLEMİ
    //butona basıldıgında this.btn_search_model.addActionListener(e -> { çalışacak olan arama
    //sorgusunu buraya yazıyoruz

    public ArrayList<Model> searchForTable(int brandId, Model.Fuel fuel, Model.Gear gear, Model.Type type) {
        String select1 = "SELECT * FROM public.model";
        ArrayList<String> whereList = new ArrayList<>();
    //buradaki if kosullarım arama için combobox lardan hangiler secilirse arama yapması için
        if (brandId !=0) {
            whereList.add("model_brand_id = " + brandId);
        }

        if (fuel != null) {
            whereList.add("model_fuel = '" + fuel.toString() + "'");
        }
        if (gear != null) {
            whereList.add("model_gear = '" + gear.toString() + "'");
        }
        if (type != null) {
            whereList.add("model_type = '" + type.toString() + "'");
        }

      String whereStr = String.join(" AND " , whereList);
        String query2 = select1 ;

        if (whereStr.length() >= 0 ) {
            query2  += " WHERE " + whereStr;
        }


        return this.modelDao.selectByQuery(query2);
        //    return this.modelDao.selectByQuery(query);

       // System.out.println(whereList);
    //     return this.modelDao.getByListBrandId(brandId);
    }

 //********************************************************************************************************

//********************************************************************************************************




}
