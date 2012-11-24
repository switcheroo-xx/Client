package se.agvard.switcheroo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class SwitcherooActivity extends Activity implements ShowErrorDialog {

    private View mButtonRefresh;
    private ListView mListDevices;
    protected AlertDialog mDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        GlobalSettings.init(getBaseContext());

        mButtonRefresh = findViewById(R.id.button_refresh);
        mButtonRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getAvailableDevices();
            }
        });

        mListDevices = (ListView) findViewById(R.id.list_devices);

        getAvailableDevices();
    }

    private void getAvailableDevices() {
        mListDevices.setAdapter(null);
        new GetAvailableDevices() {
            @Override
            protected void onPostExecute(GetAvailableDevicesResult result) {
                // Check if activity has been destroyed
                if (mListDevices == null) {
                    return;
                }

                if (result.success()) {
                    DeviceListAdapter adapter = new DeviceListAdapter(getBaseContext(),
                            result.deviceListItems, SwitcherooActivity.this);
                    mListDevices.setAdapter(adapter);
                } else {
                    showErrorDialog(result.getErrorText());
                }
            };
        }.execute();
    }

    public void showErrorDialog(String errorText) {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(SwitcherooActivity.this);
        builder.setTitle(R.string.error);
        builder.setMessage(errorText);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mDialog = null;
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    public void showSettingsDialog() {
        if (mDialog != null) {
            mDialog.dismiss();
            mDialog = null;
        }

        View view = LayoutInflater.from(this).inflate(R.layout.dialog_settings, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(SwitcherooActivity.this);
        builder.setTitle(R.string.settings);
        builder.setView(view);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String host = ((EditText) mDialog.findViewById(R.id.host)).getText().toString();

                int port;
                try {
                    port = Integer.parseInt(((EditText) mDialog.findViewById(R.id.port)).getText()
                            .toString());
                } catch (NumberFormatException e) {
                    port = 0;
                }

                String password = ((EditText) mDialog.findViewById(R.id.password)).getText()
                        .toString();

                GlobalSettings globalSettings = GlobalSettings.get();
                globalSettings.set(host, port, password);

                dialog.dismiss();
                mDialog = null;

                getAvailableDevices();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mDialog = null;
            }
        });
        mDialog = builder.create();
        mDialog.show();

        GlobalSettings globalSettings = GlobalSettings.get();
        ((EditText) mDialog.findViewById(R.id.host)).setText(globalSettings.getHost());
        final EditText editPort = ((EditText) mDialog.findViewById(R.id.port));
        ((EditText) mDialog.findViewById(R.id.password)).setText(globalSettings.getPassword());
        editPort.setText(Integer.toString(globalSettings.getPort()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                showSettingsDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDialog != null) {
            mDialog.dismiss();
        }

        mButtonRefresh.setOnClickListener(null);
        mListDevices.setAdapter(null);

        mButtonRefresh = null;
        mListDevices = null;
    }

}