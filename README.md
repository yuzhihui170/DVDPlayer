DVDPlayer 
DVDLaunchActivity


DVDService.java 
	|->onStart();
	    |->  <Zoran.java> 
		|  DVD_BRIDGE.TrgCompleted(intent, startId);
				|-> <DVDLoading.java> 
				|  DP_ENGINEER = DVDLoading.Inatance(); // 显示加载界面
				|  DP_ENGINEER.Show(mContext);          //启动DVDLoadingActivity.java
				|  DP_ENGINEER.TrgCompleted(intent, startId);
				
DVDService.java
	|-> <OnCmdCompletedListener> onCmdCompleted()
		|-> <ZoranDVD.java> 
		|  CmdCompleted(int cmd, Bundle data)
			| -> <DVDLoading.java> 
				 | -> CmdCompleted(cmd, data)