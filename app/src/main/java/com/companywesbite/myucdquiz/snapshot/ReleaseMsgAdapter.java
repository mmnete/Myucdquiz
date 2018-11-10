public class ReleaseMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> { 
	private Activity context; 
	private ArrayList<String> mList;
	private final LayoutInflater inflater;
	private static final int ITEM_TYPE_ONE = 0x00001;
	private static final int ITEM_TYPE_TWO = 0x00002; 
	
	public ReleaseMsgAdapter(Activity context, ArrayList<String> mList) { 
		this.context = context; 
		this.mList = mList; 
		inflater = LayoutInflater.from(context); 
	} 
	
	@Override 
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { 
		parent.setPadding(20, 0, 20, 0); 
		switch (viewType) { 
			case ITEM_TYPE_ONE:
				return new MyHolder(inflater.inflate(R.layout.release_message_item, parent, false)); 
			case ITEM_TYPE_TWO:
				return new MyTWOHolder(inflater.inflate(R.layout.release_message_two_item, parent, false));
			default: 
				return null; 
		} 
	} 
	
	@Override 
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) { 
		if (holder instanceof MyHolder) { 
			bindItemMyHolder((MyHolder) holder, position); 
		} else if (holder instanceof MyTWOHolder) { 
			bindItemTWOMyHolder((MyTWOHolder) holder, position); 
		} 
	} 
	
	private void bindItemTWOMyHolder(final MyTWOHolder holder, int position) { 
		LogUtils.e("Adapter", listSize() + "");
		if (listSize() >= 9) {
			holder.imageview2.setVisibility(View.GONE);
		}
		holder.imageview2.setOnClickListener(new View.OnClickListener() { 
			@Override 
			public void onClick(View v) { 
				CommonUtil.uploadPictures(context, 9 - listSize(), 0);
			}
		}); 
	} 
		
	private void bindItemMyHolder(MyHolder holder, int position) { 
		Glide.with(context) 
			.load(mList.get(position))
			.centerCrop() 
			.into(holder.imageview);
	} 
	
	@Override 
	public int getItemViewType(int position) { 
		if (position + 1 == getItemCount()) {
			return ITEM_TYPE_TWO;
		} else { 
			return ITEM_TYPE_ONE; 
		} 
	} 
	
	@Override public int getItemCount() { 
		LogUtils.e("getItemCount", mList.size() + 1 + "");
		return mList.size() + 1;
	} 
	
	class MyHolder extends RecyclerView.ViewHolder { 
		private final ImageView imageview;
		public MyHolder(View itemView) { 
			super(itemView); 
			imageview = (ImageView) itemView.findViewById(R.id.imageview);
		} 
	} 
	
	class MyTWOHolder extends RecyclerView.ViewHolder { 
		private final ImageView imageview2;
		public MyTWOHolder(View itemView) { 
			super(itemView); 
			imageview2 = (ImageView) itemView.findViewById(R.id.imageview2);
		} 
	} 
	
	public void addMoreItem(ArrayList<String> loarMoreDatas) {
		mList.addAll(loarMoreDatas); 
		notifyDataSetChanged(); 
	} 
	
	public int listSize() { 
		int size = mList.size();
		return size;
	} 
	
	public static Intent uploadPictures(Activity activity, int number,int requestCode){ 
		PhotoPickerIntent intent = new PhotoPickerIntent(activity);
		intent.setSelectModel(SelectModel.MULTI);
		intent.setShowCarema(true); 
		intent.setMaxTotal(number);
		intent.setSelectedPaths(imagePaths); 
		intent.putExtra("type","photo");
		activity.startActivityForResult(intent,requestCode); 
		return intent; 
	}
}
