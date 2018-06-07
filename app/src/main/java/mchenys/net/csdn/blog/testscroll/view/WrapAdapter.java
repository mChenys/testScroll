package mchenys.net.csdn.blog.testscroll.view;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

/**
 * 装饰者模式添加加载更多
 * Created by mChenys on 2018/6/6.
 */

public class WrapAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private RecyclerView.Adapter mAdapter;
    private View mLoadView;
    private static final int TYPE_LOADER_FOOTER = 20000;

    public WrapAdapter(RecyclerView.Adapter adapter, View loadView) {
        this.mAdapter = adapter;
        this.mLoadView = loadView;
        this.mAdapter.registerAdapterDataObserver(mObserver);
    }

    public RecyclerView.Adapter getOriginalAdapter() {
        return this.mAdapter;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_LOADER_FOOTER;
        } else {
            return mAdapter.getItemViewType(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_LOADER_FOOTER) {
            return new SimpleViewHolder(mLoadView);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < mAdapter.getItemCount()) {
            mAdapter.onBindViewHolder(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter.getItemCount() + 1;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return position == getItemCount() - 1
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
        mAdapter.onAttachedToRecyclerView(recyclerView);
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        int position = holder.getLayoutPosition();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && position == getItemCount() - 1) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
        mAdapter.onViewAttachedToWindow(holder);
    }

    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }


    private RecyclerView.AdapterDataObserver mObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            WrapAdapter.this.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            WrapAdapter.this.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            WrapAdapter.this.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            WrapAdapter.this.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            WrapAdapter.this.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            WrapAdapter.this.notifyDataSetChanged();
        }
    };
}
