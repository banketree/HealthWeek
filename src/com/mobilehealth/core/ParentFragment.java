package com.mobilehealth.core;

import com.siat.healthweek.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ParentFragment extends Fragment implements ChildPageMessageListener, ParentPageMessageListener{
	
	protected ImageView ivContainerFrameBg;
	
	protected String[] childFragmentArray;
	
	private int curPageIndex=0;
	private int prevPageIndex=0;
	
	protected int firstLevelIndex=0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		return inflater.inflate(R.layout.page_container, container, false);
		
		//return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		init(view);
		
		Fragment newPage=getChildFragmentManager().findFragmentByTag(childFragmentArray[curPageIndex]);
		if(newPage==null)
		{
			FragmentTransaction transac=getChildFragmentManager().beginTransaction();
			
			newPage=Fragment.instantiate(getActivity(), childFragmentArray[curPageIndex]);
			transac.setCustomAnimations(0, 0, 0, R.anim.view_disappear);
			transac.add(R.id.rlMain, newPage, childFragmentArray[curPageIndex]);
			
			transac.commit();
		}
		
		childPageChanged(firstLevelIndex, curPageIndex);
		
		super.onViewCreated(view, savedInstanceState);
	}
	
	protected void init(View layout)
	{
		ivContainerFrameBg=(ImageView)layout.findViewById(R.id.ivContainerFrameBg);
		
		firstLevelIndex=0;
	}
	
	private boolean changeToPageLocal(int toIndex)
	{
		if(toIndex==curPageIndex|toIndex<0)
		{
			return false;
		}

		FragmentTransaction transac=getChildFragmentManager().beginTransaction();
		
		Fragment newPage=Fragment.instantiate(getActivity(), childFragmentArray[toIndex]);
		if(toIndex>curPageIndex)
		{
			transac.setCustomAnimations(0, R.anim.view_disappear, 0, R.anim.view_disappear);
		}else
		{
			transac.setCustomAnimations(0, 0, 0, R.anim.view_disappear);
		}
		
		transac.replace(R.id.rlMain, newPage, childFragmentArray[toIndex]);
		
		if(toIndex>curPageIndex)
		{
			transac.addToBackStack(null);
			prevPageIndex=curPageIndex;
		}
		
		curPageIndex=toIndex;
		
		transac.commit();
		
		childPageChanged(firstLevelIndex, curPageIndex);
		
		return true;
	}

	@Override
	public void changeToPage(int toIndex) {
		// TODO Auto-generated method stub
		changeToPageLocal(toIndex);
	}

	@Override
	public void childPageChanged(int firstLeveIndex, int secondLevelIndex) {
		// TODO Auto-generated method stub
		((ChildPageMessageListener)getActivity()).childPageChanged(firstLeveIndex, secondLevelIndex);
	}

	@Override
	public boolean onBack() {
		// TODO Auto-generated method stub
		FragmentManager childFragManager=getChildFragmentManager();
		if(childFragManager.getBackStackEntryCount()>0)
		{
			childFragManager.popBackStack();
			curPageIndex=prevPageIndex;
			
			childPageChanged(firstLevelIndex, curPageIndex);
			
			return true;
		}
		return false;
	}
}
