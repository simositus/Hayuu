package com.tubes.sandangin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TableLayout;

import com.tubes.sandangin.R;
import com.tubes.sandangin.adapters.SubcategoryGridAdapter;
import com.tubes.sandangin.interfaces.ShowBackButton;
import com.tubes.sandangin.interfaces.ToolbarTitle;
import com.tubes.sandangin.pojo.Category;
import com.tubes.sandangin.utils.Constants;

import java.util.List;

public class Subcategories extends Fragment {

    ChildCategories childCategoriesCallback;
    ToolbarTitle toolbarTitleCallback;
    ShowBackButton showBackButtonCallback;

    // interface save child categories state
    public interface ChildCategories {
        void saveChildCategories(List<Category> childCategories);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.product_list, container, false);

        // Hide Filter Layout
        TableLayout sortFilter = view.findViewById(R.id.sortFilter);
        sortFilter.setVisibility(View.GONE);

        // get data
        Bundle args = getArguments();
        assert args != null;
        List<Category> childCategories = (List<Category>) args.getSerializable(Constants.CAT_KEY);
        childCategoriesCallback.saveChildCategories(childCategories);

        // show back button
        showBackButtonCallback.showBackButton();

        // set toolbar title
        toolbarTitleCallback.setToolbarTitle(args.getString(Constants.TITLE));
        toolbarTitleCallback.saveSubcategoryTitle(args.getString(Constants.TITLE));

        // fill gridview with data
        GridView gv = view.findViewById(R.id.productsGrid);
        assert childCategories != null;
        if (childCategories.size() >= 3) {
            gv.setNumColumns(3);
        } else if (childCategories.size() >= 2) {
            gv.setNumColumns(2);
        } else {
            gv.setNumColumns(1);
        }
        gv.setAdapter(new SubcategoryGridAdapter(getActivity(), childCategories));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        childCategoriesCallback = (ChildCategories) context;
        toolbarTitleCallback = (ToolbarTitle) context;
        showBackButtonCallback = (ShowBackButton) context;
    }
}
