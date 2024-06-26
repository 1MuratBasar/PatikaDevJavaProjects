package view;

import business.BrandManager;
import business.CarManager;
import business.ModelManager;
import core.ComboItem;
import core.Helper;
import entity.Brand;
import entity.Car;
import entity.Model;
import entity.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class AdminView extends Layout {

    private JPanel container;
    private JLabel lbl_welcome;
    private JPanel pnl_top;
    private JTabbedPane tab_menu;
    private JButton btn_logout;
    private JPanel pnl_brand;
    private JTable tbl_brand;
    private JPanel pnl_model;
    private JScrollPane scrl_model;
    private JTable tbl_model;
    private JButton btn_search_model;
    private JComboBox<ComboItem> cmb_s_model_brand; // combobo xa markayı getircez
    private JComboBox<Model.Type> cmb_s_model_type;
    private JComboBox<Model.Fuel> cmb_s_model_fuel;
    private JComboBox <Model.Gear> cmb_s_model_gear;
    private JButton btn_cncl_model;
    private JPanel pnl_cars;
    private JScrollPane scl_car;
    private JTable tbl_car;
    private JPanel pnl_booking;
    private JScrollPane scrl_booking;
    private JTable tbl_booking;
    private JPanel pnl_booking_search;
    private JComboBox cmb_booking_gear;
    private JComboBox cmb_booking_fuel;
    private JComboBox cmb_booking_type;
    private JFormattedTextField fld_strt_date;
    private JFormattedTextField fld_fnsh_date;
    private JButton btn_booking_search;
    private JButton btn_cncl_booking;
    // private JTable tbl_car1;
    private User user;
    private DefaultTableModel tmdl_brand = new DefaultTableModel();
    private DefaultTableModel tmdl_model = new DefaultTableModel();
    private DefaultTableModel tmdl_car = new DefaultTableModel();
    private DefaultTableModel tmdl_booking = new DefaultTableModel();



    private BrandManager brandManager;
    private ModelManager modelManager;
    private CarManager carManager;
    private JPopupMenu brand_menu;
    private JPopupMenu model_menu;
    private JPopupMenu car_menu;
    private JPopupMenu booking_menu;
    private Object[] col_model;
    private Object[] col_car;




    public AdminView(User user) {
        this.carManager = new CarManager();
        this.brandManager = new BrandManager();
        this.modelManager = new ModelManager();
        this.add(container);
        this.guiInitialize(1000,500);
        this.user = user;

        if (this.user == null) {
            dispose();
        }

        this.lbl_welcome.setText("Hoşgeldiniz : " + this.user.getUsername());

        //General Codes
        loadComponent();
        loadBrandTable();
        loadBrandComponent ();

        loadModelTable (null);
        loadModelComponent();
        loadModelFilter();

        //Car Tab Menu
         loadCarTable();
         loadCarComponent();

         //Booking Tab Menu
        loadBookingTable(null);
        loadBookingComponent();
        loadBookingFilter();


    }

    private void loadComponent() {
        this.btn_logout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LoginView loginView = new LoginView();
            }
        });
    }

    private void loadBookingFilter() {

        this.cmb_booking_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
        this.cmb_booking_type.setSelectedItem(null);
        this.cmb_booking_gear.setModel(new DefaultComboBoxModel<>(Model.Gear.values()));
        this.cmb_booking_gear.setSelectedItem(null);
        this.cmb_booking_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
        this.cmb_booking_fuel.setSelectedItem(null);
    }

    private void loadBookingTable(ArrayList<Object[]> carList) {

        Object[] col_booking_list = {"ID", "Marka", "Model", "Plaka", "Renk", "KM", "Yıl", "Tip", "Yakıt Türü", "Vites"};
        createTable(this.tmdl_booking, this.tbl_booking, col_booking_list, carList);

    }

    private void loadBookingComponent() {

        tableRowSelect(this.tbl_booking);
        this.booking_menu = new JPopupMenu();
        this.booking_menu.add("Rezervasyon Yap").addActionListener(e -> {


            //buraya secilmiş olan degerlerinv erileri girilecek

            int selectCarId = this.getTableSelectedRow(this.tbl_booking, 0);

            BookingView bookingView = new BookingView(

                    this.carManager.getById(selectCarId),
                    this.fld_strt_date.getText(),
                    this.fld_fnsh_date.getText()

            );


            // işlemleerin sonunda booking viewvin windowlistener dinlicez
            bookingView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBookingTable(null);
                    loadBookingFilter();
                }
            });

        });

    this.tbl_booking.setComponentPopupMenu(booking_menu);

        //arabalaarı array e atıp bunları tablolara obje seklidne göndericez
        btn_booking_search.addActionListener(e -> {
            ArrayList<Car> carList = this.carManager.searchForBooking(
                    fld_strt_date.getText(),
                    fld_fnsh_date.getText(),
                    (Model.Type) cmb_booking_type.getSelectedItem(),
                    (Model.Gear) cmb_booking_gear.getSelectedItem(),
                    (Model.Fuel) cmb_booking_fuel.getSelectedItem()

            );
            //tablonun içerisinde satırları olustur
            ArrayList<Object[]> carBookingRow = this.carManager.getForTable(this.col_car.length, carList);
            loadBookingTable(carBookingRow);

        });
        btn_cncl_booking.addActionListener(e -> {
            loadBookingFilter();
        });
    }

    private void loadCarComponent() {
        tableRowSelect(this.tbl_car);
        this.car_menu = new JPopupMenu();
        this.car_menu.add("Yeni").addActionListener(e -> {
            CarView carView = new CarView(new Car());
            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCarTable();
                }
            });
        });
        this.car_menu.add("Güncelle").addActionListener(e -> {
            int selectModelId = this.getTableSelectedRow(tbl_car,0);
            CarView carView = new CarView(this.carManager.getById(selectModelId));
            carView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadCarTable();
                }
            });
        });
        this.car_menu.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectCarId = this.getTableSelectedRow(tbl_car,0);
                if (this.carManager.delete(selectCarId)){
                    Helper.showMsg("done");
                    loadCarTable();
                } else {
                    Helper.showMsg("error");
                }
            }
        });
        this.tbl_car.setComponentPopupMenu(car_menu);
    }

    private void loadCarTable() {
        col_car = new Object[]{"ID", "Marka", "Model", "Plaka", "Renk", "KM", "Yıl", "Tip", "Yakıt Türü", "Vites"};
        ArrayList<Object[]> carList = this.carManager.getForTable(col_car.length, this.carManager.findAll());
        createTable(this.tmdl_car, this.tbl_car, col_car, carList);
    }



    private void loadModelFilter() {
    this.cmb_s_model_type.setModel(new DefaultComboBoxModel<>(Model.Type.values()));
    this.cmb_s_model_type.setSelectedItem(null);
    this.cmb_s_model_gear.setModel(new DefaultComboBoxModel<>(Model.Gear.values()));
    this.cmb_s_model_gear.setSelectedItem(null);
    this.cmb_s_model_fuel.setModel(new DefaultComboBoxModel<>(Model.Fuel.values()));
    this.cmb_s_model_fuel.setSelectedItem(null);
        loadModelFilterBrand();
    }
    //markayı comboya getir
    public void loadModelFilterBrand(){

        this.cmb_s_model_brand.removeAllItems();
        for (Brand obj : brandManager.findAll()) {
            this.cmb_s_model_brand.addItem(new ComboItem(obj.getId(), obj.getName()));
        }
        this.cmb_s_model_brand.setSelectedItem(null);

    }

    private void loadModelComponent() {
        tableRowSelect (this.tbl_model);
        this.model_menu = new JPopupMenu();
        this.model_menu.add("Yeni").addActionListener(e -> {
            ModelView modelView = new ModelView(new Model());
            modelView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadModelTable(null);
                }
            });

        });
        this.model_menu.add("Güncelle").addActionListener(e -> {
            int selectModelId = this.getTableSelectedRow(tbl_model,0);
            ModelView modelView = new ModelView(this.modelManager.getById(selectModelId));
            modelView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadModelTable(null);
                    //ödev için loadBookTable (null);

                }
            });

        });
        this.model_menu.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectModelId = this.getTableSelectedRow(tbl_model,0);
                if (this.modelManager.delete(selectModelId)){
                    Helper.showMsg("done");
                    loadModelTable(null);
                } else {
                    Helper.showMsg("error");
                }
            }

        });
        this.tbl_model.setComponentPopupMenu(model_menu);

        this.btn_search_model.addActionListener(e -> {
        ComboItem selectedBrand  = (ComboItem) this.cmb_s_model_brand.getSelectedItem();
        int branId = 0;
        if (selectedBrand != null) {
            branId = selectedBrand.getKey();
        }
        ArrayList<Model> modelListBySearch = this.modelManager.searchForTable(
                branId,
                (Model.Fuel) cmb_s_model_fuel.getSelectedItem(),
                (Model.Gear) cmb_s_model_gear.getSelectedItem(),
                (Model.Type) cmb_s_model_type.getSelectedItem()

        );

        ArrayList<Object[]> modelRowListBySearch = this.modelManager.getForTable(this.col_model.length ,
                modelListBySearch);
        loadModelTable(modelRowListBySearch);

        });

        this.btn_cncl_model.addActionListener(e -> {
            this.cmb_s_model_type.setSelectedItem(null);
            this.cmb_s_model_gear.setSelectedItem(null);
            this.cmb_s_model_fuel.setSelectedItem(null);
            this.cmb_s_model_brand.setSelectedItem(null);
            loadModelTable(null);

        });

    }

    public void loadModelTable (ArrayList<Object[]> modelList) {

       this.col_model = new Object[] {"Model ID", "Marka", "Model Adı", "Tip", "Yıl", "Yakıt Türü", "Vites"};
        if (modelList == null) {
            modelList = this.modelManager.getForTable(this.col_model.length, this.modelManager.findAll());

        }
        createTable(this.tmdl_model, this.tbl_model, this.col_model, modelList);
    }

    public void loadBrandComponent () {


        tableRowSelect(this.tbl_brand);
        /* üstteki metot yazılarak alttaki fazla kod azaltıldı
        this.tbl_brand.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int selected_row = tbl_brand.rowAtPoint(e.getPoint());
                tbl_brand.setRowSelectionInterval(selected_row, selected_row);

            }
        });

        */
        /*
        JMenuItem newBrandItem = new JMenuItem("Yeni");
        this.brandMenu.add(newBrandItem);
        newBrandItem.addActionListener(e -> {
            System.out.println("yeni butonu tıklandı");
        });
        */
        this.brand_menu = new JPopupMenu();
        this.brand_menu.add("Yeni").addActionListener(e -> {
            BrandView brandView = new BrandView(null);
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilter();     //markayı comboya getir

                }
            });
        });
        this.brand_menu.add("Güncelle").addActionListener(e -> {
            int selectBrandId = this.getTableSelectedRow(tbl_brand,0);
            BrandView brandView = new BrandView(this.brandManager.getById(selectBrandId));
            brandView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilter();     //markayı comboya getir
                    loadCarTable();
                    //ödev için loadBookTable (null);

                }
            });
        });
        this.brand_menu.add("Sil").addActionListener(e -> {
            if (Helper.confirm("sure")) {
                int selectBrandId = this.getTableSelectedRow(tbl_brand,0);
                if (this.brandManager.delete(selectBrandId)){
                    Helper.showMsg("done");
                    loadBrandTable();
                    loadModelTable(null);
                    loadModelFilter();     //markayı comboya getir
                    loadCarTable();
                    //ödev için loadBookTable (null);

                } else {
                    Helper.showMsg("error");
                }
            }


        });
           this.tbl_brand.setComponentPopupMenu(brand_menu);

    }


    public void loadBrandTable() {
        Object[] col_brand = {"Marka ID", "Marka Adı"};
      //  ArrayList<Brand> brandList = this.brandManager.findAll();
        ArrayList <Object[]> brandList = this.brandManager.getForTable(col_brand.length);
        this.createTable(this.tmdl_brand, this.tbl_brand, col_brand, brandList);

      /*
        this.tmdl_brand.setColumnIdentifiers(col_brand);
        this.tbl_brand.setModel(tmdl_brand);
        this.tbl_brand.getTableHeader().setReorderingAllowed(false);
        this.tbl_brand.setEnabled(false);
        DefaultTableModel clearModel = (DefaultTableModel) tbl_brand.getModel();
        clearModel.setRowCount(0); // her defasında tablo içini boşaltacak
        for (Brand brand : brandList){
            Object[] obj = {brand.getId(), brand.getName()};
            this.tmdl_brand.addRow(obj);
        }

        */
    }

    private void createUIComponents() throws Exception{
        // TODO: place custom component creation code here
        this.fld_strt_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        this.fld_strt_date.setText("10/10/2023");
        this.fld_fnsh_date = new JFormattedTextField(new MaskFormatter("##/##/####"));
        this.fld_fnsh_date.setText("25/10/2023");
    }
}
