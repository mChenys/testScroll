package mchenys.net.csdn.blog.testscroll.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mchenys.net.csdn.blog.testscroll.R;
import mchenys.net.csdn.blog.testscroll.view.LoaderRecycleView;

/**
 * Created by mChenys on 2018/6/4.
 */

public class TabFrament extends Fragment {
    private int position;
    private List<String> mData = new ArrayList<>();
    private MyAdapter mAdapter;
    private LoaderRecycleView mLoaderRecycleView;

    public static TabFrament newInstance(int position) {
        TabFrament f = new TabFrament();
        Bundle b = new Bundle();
        b.putInt("position", position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
        for (int i = 0; i < 20; i++) {
            mData.add("第" + (position + 1) + "页,第" + (i + 1) + "条数据");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.e("cys", "onCreateView -->" + position);
        View root = inflater.inflate(R.layout.fragment_tab, container, false);
        mLoaderRecycleView = root.findViewById(R.id.rv);
        mLoaderRecycleView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mLoaderRecycleView.setAdapter(mAdapter = new MyAdapter());
        mLoaderRecycleView.setOnLoaderMoreListener(new LoaderRecycleView.OnLoaderMoreListener() {
            @Override
            public void onMore(final LoaderRecycleView rv) {
                if (mData.size() >= 30) {
                    rv.loadNoMore();
                } else {
                    rv.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            List<String> temp = new ArrayList<>();
                            for (int i = 0; i < 10; i++) {
                                temp.add("第" + (position + 1) + "页,第" + (i + 1) + "新的条数据");
                            }
                            mData.addAll(temp);
                            rv.loadComplete();
                        }
                    }, 1000);
                }

            }
        });
        return root;
    }

    public void refresh() {
        mData.clear();
        for (int i = 0; i < 10; i++) {
            mData.add("第" + (position + 1) + "页,第" + (i + 1) + "条数据");
        }
        mLoaderRecycleView.loadComplete();
    }


    public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(getLayoutInflater().inflate(R.layout.item_list, parent, false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv_info);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.e("cys", "setUserVisibleHint:" + getUserVisibleHint() + " -->" + position);
    }
}


