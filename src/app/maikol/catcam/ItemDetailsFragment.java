package app.maikol.catcam;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import app.maikol.catcam.components.ViewPagerDelegate;
import app.maikol.catcam.delegate.RemoteImageDelegate;
import app.maikol.catcam.model.Comment;
import app.maikol.catcam.model.PublicPhoto;
import app.maikol.catcam.util.HttpConnectionManager;
import app.maikol.catcam.util.Util;

public class ItemDetailsFragment extends Fragment implements
		RemoteImageDelegate {

	ViewPagerDelegate mViewPagerDelegate;

	ImageButton mtoLeftButton;
	ImageButton mtoRightButton;
	Button mNewImagesButton;
	Button mMyCloudImagesButton;
	TextView mTxtUserName, mTxtDescription;
	EditText mCommentInputText;
	Button mCommentButton;

	LinearLayout mCommentListView;

	ImageView image;

	String mSavedUsername;

	List<Comment> mCommentList = new ArrayList<Comment>();
	CommentAdapter mCommentAdapter;

	PublicPhoto mPublicPhoto;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final SharedPreferences options = getActivity().getPreferences(
				Context.MODE_PRIVATE);
		final SharedPreferences.Editor editor = options.edit();

		ScrollView imageDetailsLayout = (ScrollView) inflater.inflate(
				R.layout.imagedetails, container, false);
		mViewPagerDelegate = (CustomPagerActivity) this.getActivity();
		mTxtUserName = (TextView) imageDetailsLayout
				.findViewById(R.id.imagedetails_txt_username);
		mTxtDescription = (TextView) imageDetailsLayout
				.findViewById(R.id.imagedetails_txt_description);
		mCommentListView = (LinearLayout) imageDetailsLayout
				.findViewById(R.id.imagedetails_comment_list);
		mCommentInputText = (EditText) imageDetailsLayout
				.findViewById(R.id.imagedetails_comment_input);
		mCommentButton = (Button) imageDetailsLayout
				.findViewById(R.id.imagedetails_comment_button);

		mCommentButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String comment = String.valueOf(mCommentInputText.getText());
				Comment newComment = new Comment();
				newComment.setComment(comment);
				newComment.setUsername(Util
						.getMyUsername(ItemDetailsFragment.this.getActivity()));
				newComment.setImageId(mPublicPhoto.getId());

				HttpConnectionManager.saveComment(newComment);

				mCommentList.add(newComment);
				mCommentAdapter.notifyDataSetChanged();
				mCommentInputText.setText("");
				
				InputMethodManager imm = (InputMethodManager)ItemDetailsFragment.this.getActivity().getSystemService(
					      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(mCommentInputText.getWindowToken(),0);
			}
		});

		mCommentInputText.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
		mCommentInputText
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						if (actionId == EditorInfo.IME_ACTION_DONE) {

							// if (mPublicPhoto != null) {
							//
							// String comment = String.valueOf(v.getText());
							// Comment newComment = new Comment();
							// newComment.setComment(comment);
							// newComment.setUsername(Util
							// .getMyUsername(ItemDetailsFragment.this
							// .getActivity()));
							// newComment.setImageId(mPublicPhoto.getId());
							//
							// HttpConnectionManager.saveComment(newComment);
							//
							// commentList.add(newComment);
							// mCommentAdapter.notifyDataSetChanged();
							// v.setText("");
							//
							// }

							v.clearFocus();
						}
						return false;
					}
				});

		mCommentAdapter = new CommentAdapter(this.getActivity(), mCommentList);

		// mCommentListView.setAdapter(mCommentAdapter);

		image = (ImageView) imageDetailsLayout
				.findViewById(R.id.imagedetails_image);

		return imageDetailsLayout;
	}

	public void setPhoto(PublicPhoto photo) {

		this.mPublicPhoto = photo;
		image.setImageBitmap(photo.getBitmap());

		mTxtUserName.setText(photo.getUser());
		mTxtDescription.setText(photo.getDescription());

		HttpConnectionManager.getCommentsFromPhoto(mPublicPhoto.getId(), this);
	}

	@Override
	public void didGetImage(PublicPhoto photo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didGetImage(Integer index, PublicPhoto photo) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didFailGetImage(int i) {
		// TODO Auto-generated method stub

	}

	@Override
	public void didGetComment(int i, Comment comment) {
		if (mCommentList == null) {
			mCommentList = new ArrayList<Comment>();
		}
		mCommentList.add(comment);

		mCommentListView.addView(mCommentAdapter.getView(
				mCommentList.size() - 1, null, null));
	}

	@Override
	public void didFailGetComment(int i) {
		// TODO Auto-generated method stub

	}

	public class CommentAdapter extends BaseAdapter {

		List<Comment> commentList = new ArrayList<Comment>();

		private Context context;

		CommentAdapter(Context context, List<Comment> cl) {
			super();
			this.context = context;
			this.commentList = cl;
		}

		@Override
		public int getCount() {
			return commentList.size();
		}

		@Override
		public Comment getItem(int pos) {
			return commentList.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@Override
		public View getView(int pos, View view, ViewGroup parent) {
			System.out.println("Drawing view in  pos" + pos + " of "
					+ commentList.size());
			Comment comment = getItem(pos);

			View v = view;

			if (v == null) {

				LayoutInflater vi = (LayoutInflater) this.context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				v = vi.inflate(R.layout.imagedetails_comment, null);

				TextView usernameView = (TextView) v
						.findViewById(R.id.imagedetails_comment_username);
				TextView commentTextView = (TextView) v
						.findViewById(R.id.imagedetails_comment_text);

				usernameView.setText(comment.getUsername());
				commentTextView.setText(comment.getComment());
//				mCommentListView.addView(v);
			}

			return v;
		}

	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden){
			mViewPagerDelegate.removeImageDetailsFragment();
		}
	}
}
