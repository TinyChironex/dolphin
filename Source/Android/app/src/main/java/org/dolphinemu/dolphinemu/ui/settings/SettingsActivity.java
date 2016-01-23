package org.dolphinemu.dolphinemu.ui.settings;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.dolphinemu.dolphinemu.BuildConfig;
import org.dolphinemu.dolphinemu.R;
import org.dolphinemu.dolphinemu.model.settings.SettingSection;

import java.util.HashMap;

public final class SettingsActivity extends AppCompatActivity implements SettingsActivityView
{
	private SettingsActivityPresenter mPresenter = new SettingsActivityPresenter(this);


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_settings);

		Intent launcher = getIntent();
		String filename = launcher.getStringExtra(ARGUMENT_FILE_NAME);

		mPresenter.onCreate(savedInstanceState, filename);
	}

	/**
	 * If this is called, the user has left the settings screen (potentially through the
	 * home button) and will expect their changes to be persisted. So we kick off an
	 * IntentService which will do so on a background thread.
	 */
	@Override
	protected void onStop()
	{
		super.onStop();

		mPresenter.onStop(isFinishing());
	}

	@Override
	public void showSettingsFragment(String menuTag, boolean addToStack)
	{
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
				.replace(R.id.frame_content, SettingsFragment.newInstance(menuTag), SettingsFragment.FRAGMENT_TAG);

		if (addToStack)
		{
			transaction.addToBackStack(null);
		}

		transaction.commit();
	}

	@Override
	public HashMap<String, SettingSection> getSettings()
	{
		return mPresenter.getSettings();
	}

	@Override
	public void setSettings(HashMap<String, SettingSection> settings)
	{
		mPresenter.setSettings(settings);
	}

	@Override
	public void onSettingsFileLoaded(HashMap<String, SettingSection> settings)
	{
		SettingsFragmentView fragment = getFragment();

		if (fragment != null)
		{
			fragment.onSettingsFileLoaded(settings);
		}
	}

	@Override
	public void showToastMessage(String message)
	{
		Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
	}

	private SettingsFragment getFragment()
	{
		return (SettingsFragment) getSupportFragmentManager().findFragmentByTag(SettingsFragment.FRAGMENT_TAG);
	}

	public static final String ARGUMENT_FILE_NAME = BuildConfig.APPLICATION_ID + ".file_name";

	public static void launch(Context context, String menuTag)
	{
		Intent settings = new Intent(context, SettingsActivity.class);

		settings.putExtra(ARGUMENT_FILE_NAME, menuTag);

		context.startActivity(settings);
	}
}
