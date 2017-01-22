package gbc.sa.vansales.data;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gbc.sa.vansales.activities.LoadRequestConstants;
import gbc.sa.vansales.models.Customer;
import gbc.sa.vansales.models.ColletionData;
import gbc.sa.vansales.models.CustomerData;
import gbc.sa.vansales.models.PreSaleProceed;
import gbc.sa.vansales.models.Sales;
import gbc.sa.vansales.models.ShelfProduct;

/**
 * Created by eheuristic on 12/2/2016.
 */

public class Const {
    public static ArrayList<Customer> dataArrayList;
    public static ArrayList<Customer> allCustomerdataArrayList;
    public static  ArrayList<String> addlist=new ArrayList<>();
    public static ArrayList<Sales> salesarrayList;

    public static List<LoadRequestConstants> loadRequestConstantsList=new ArrayList<>();
    public static HashMap<Integer,List<LoadRequestConstants>> constantsHashMap=new HashMap<>();


    public static ArrayList<ColletionData> colletionDatas = new ArrayList<>();


    public static ArrayList<PreSaleProceed> proceedArrayList=new ArrayList<>();
    public static String availableLimit = "";

    public static int customerPosition;

    public static int id=-1;

    public static Bundle grBundle;
    public static Bundle brBundle;
    public static Bundle siBundle;
 }
