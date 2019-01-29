package xyz.glucn.notebook;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private CursorAdapter mCursorAdapter;
    private Context mContext;

    MyRecyclerAdapter(Context ctx, CursorAdapter cursorAdapter) {
        mContext = ctx;
        mCursorAdapter = cursorAdapter;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = mCursorAdapter.newView(mContext, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mContext, mCursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View mView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
    }


}
