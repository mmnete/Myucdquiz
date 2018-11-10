public class ReleaseMessageActivity extends AppCompatActivity {
	@BindView(R.id.text_cancel) 
	TextView textCancel; 
	@BindView(R.id.text_release)
	TextView textRelease;
	@BindView(R.id.mRec)
	RecyclerView mRec; 
	@BindView(R.id.et_messsage) 
	EditText etMesssage; 
	private ArrayList<String> listImagePath; 
	private ArrayList<String> mList = new ArrayList<>();
	private ReleaseMsgAdapter adapter; 
	
	private ArrayList<String> list = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_release_message);
		ButterKnife.bind(this); 
		setRecyclerview(); 
	} 
	
	private void setRecyclerview() { 
		if (mList != null) {
			GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
			mRec.setLayoutManager(gridLayoutManager); 
			adapter = new ReleaseMsgAdapter(ReleaseMessageActivity.this, mList); 
			mRec.setAdapter(adapter); 
			} 
	} 
	
	@OnClick({R.id.text_cancel, R.id.text_release})
	public void onClick(View view) { 
		switch (view.getId()) {
			case R.id.text_cancel: 
				ToastUtil.showToast("取消"); 
				break;
			case R.id.text_release:
				ToastUtil.showToast("发布"); 
				break; 
		} 
	} 
	
	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		if (requestCode == 0 && resultCode == RESULT_OK) { 
			listImagePath = data.getStringArrayListExtra(EXTRA_RESULT); 
			compress(listImagePath); 
		} 
	} 
	
	public void compress(ArrayList<String> list) { 
		for (String imageUrl : list) { 
			LogUtils.e(">>>>>>", imageUrl);
			File file = new File(imageUrl);
			compressImage(file); 
		} 
		adapter.addMoreItem(list); 
	} 
	
	private void compressImage(File file) { 
		Luban.get(this)
			.load(file) 
			.putGear(Luban.THIRD_GEAR) 
			.setCompressListener(new OnCompressListener() { 
				@Override
				public void onStart() { 
				} 
				
				@Override
				public void onSuccess(final File file) { 
					URI uri = file.toURI(); 
					String[] split = uri.toString().split(":");
					list.add(split[1]);
					LogUtils.e(BaseApplication.TAG, uri + "????????????" + split[1]); 
				} 
				
				@Override 
				public void onError(Throwable e) { 
				} 
			}).launch();
	} 
}
