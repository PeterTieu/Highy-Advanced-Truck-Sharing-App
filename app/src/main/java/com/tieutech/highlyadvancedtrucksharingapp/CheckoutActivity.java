package com.tieutech.highlyadvancedtrucksharingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.tieutech.highlyadvancedtrucksharingapp.R;
import com.tieutech.highlyadvancedtrucksharingapp.util.Util;
import com.tieutech.highlyadvancedtrucksharingapp.viewmodel.CheckoutViewModel;

import java.util.Locale;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Checkout implementation for the app
 */
public class CheckoutActivity extends AppCompatActivity {

    // Arbitrarily-picked constant integer you define to track a request for payment data activity.
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    private CheckoutViewModel model;

    //View variables
    ImageView orderDetailsUserImageImageView;
    TextView orderDetailsSenderNameTextView;
    TextView orderDetailsPickupTimeTextView;
    TextView orderDetailsPickupDateTextView;
    TextView orderDetailsPickupLocationEditText;
    TextView orderDetailsReceiverNameTextView;
    TextView orderDetailsDestinationTextView;
    TextView orderDetailsGoodTypeTextView;
    TextView orderDetailsVehicleTypeTextView;
    TextView orderDetailsWeightTextView;
    TextView orderDetailsHeightTextView;
    TextView orderDetailsWidthTextView;
    TextView orderDetailsLengthTextView;
    Button callDriverButton;

    ImageView orderDetailsGoodImageImageView;
    TextView goodClassificationInfoTextView;

    //Data variables
    private View googlePayButton;
    byte[] senderImageByteArray;
    String senderName;
    String pickupTime;
    String pickupDate;
    double destinationLatitude, destinationLongitude;
    double pickupLocationLatitude, pickupLocationLongitude;
    String pickupLocation, destination;
    String receiverName;
    String goodType;
    String vehicleType;
    String weight;
    String height;
    String width;
    String length;

    byte[] goodImage;
    String goodClassification;
    double goodClassificationConfidence;

    /**
     * Initialize the Google Pay API on creation of the activity
     *
     * @see AppCompatActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeUi();

        model = new ViewModelProvider(this).get(CheckoutViewModel.class);
        model.canUseGooglePay.observe(this, this::setGooglePayAvailable);
    }

    private void initializeUi() {
        setContentView(R.layout.activity_order_details);

        //Obtain views
        orderDetailsUserImageImageView = (ImageView) findViewById(R.id.orderDetailsUserImageImageView);
        orderDetailsSenderNameTextView = (TextView) findViewById(R.id.orderDetailsSenderNameTextView);
        orderDetailsPickupTimeTextView = (TextView) findViewById(R.id.orderDetailsPickupTimeTextView);
        orderDetailsPickupDateTextView = (TextView) findViewById(R.id.orderDetailsPickupDateTextView);
        orderDetailsPickupLocationEditText = (TextView) findViewById(R.id.orderDetailsPickupLocationEditText);
        orderDetailsReceiverNameTextView = (TextView) findViewById(R.id.orderDetailsReceiverNameTextView);
        orderDetailsDestinationTextView = (TextView) findViewById(R.id.orderDetailsDestinationTextView);
        orderDetailsGoodTypeTextView = (TextView) findViewById(R.id.orderDetailsGoodTypeTextView);
        orderDetailsVehicleTypeTextView = (TextView) findViewById(R.id.orderDetailsVehicleTypeTextView);
        orderDetailsWeightTextView = (TextView) findViewById(R.id.orderDetailsWeightTextView);
        orderDetailsHeightTextView = (TextView) findViewById(R.id.orderDetailsHeightTextView);
        orderDetailsWidthTextView = (TextView) findViewById(R.id.orderDetailsWidthTextView);
        orderDetailsLengthTextView = (TextView) findViewById(R.id.orderDetailsLengthTextView);
        callDriverButton = (Button) findViewById(R.id.getEstimateButton);
        googlePayButton = findViewById(R.id.googlePayButton);

        orderDetailsGoodImageImageView = (ImageView) findViewById(R.id.orderDetailsGoodImageImageView);
        goodClassificationInfoTextView = (TextView) findViewById(R.id.goodClassificationInfoTextView);

        //Obtain data passed from OrderDetailsActivity
        Intent intent = getIntent();
        senderImageByteArray = intent.getByteArrayExtra(Util.DATA_SENDER_IMAGE);
        senderName = intent.getStringExtra(Util.DATA_SENDER_NAME);
        pickupDate = intent.getStringExtra(Util.DATA_PICKUP_DATE);
        pickupTime = intent.getStringExtra(Util.DATA_PICKUP_TIME);
        pickupLocation = intent.getStringExtra(Util.DATA_PICKUP_LOCATION);
        pickupLocationLatitude = intent.getDoubleExtra(Util.DATA_PICKUP_LOCATION_LATITUDE, 0);
        pickupLocationLongitude = intent.getDoubleExtra(Util.DATA_PICKUP_LOCATION_LONGITUDE, 0);
        destination = intent.getStringExtra(Util.DATA_DESTINATION);
        destinationLatitude = intent.getDoubleExtra(Util.DATA_DESTINATION_LATITUDE, 0);
        destinationLongitude = intent.getDoubleExtra(Util.DATA_DESTINATION_LONGITUDE, 0);
        receiverName = intent.getStringExtra(Util.DATA_RECEIVER_NAME);
        goodType = intent.getStringExtra(Util.DATA_GOOD_TYPE);
        vehicleType = intent.getStringExtra(Util.DATA_VEHICLE_TYPE);
        weight = intent.getStringExtra(Util.DATA_WEIGHT);
        height = intent.getStringExtra(Util.DATA_HEIGHT);
        width = intent.getStringExtra(Util.DATA_WIDTH);
        length = intent.getStringExtra(Util.DATA_LENGTH);

        goodImage = intent.getByteArrayExtra(Util.DATA_GOOD_IMAGE);
        goodClassification = intent.getStringExtra(Util.DATA_GOOD_CLASSIFICATION);
        goodClassificationConfidence = intent.getDoubleExtra(Util.DATA_GOOD_CLASSIFICATION_CONFIDENCE, 0);


        //Display data in the views
        callDriverButton.setVisibility(View.INVISIBLE);
        orderDetailsUserImageImageView.setImageBitmap(Util.getBitmapFromBytesArray(senderImageByteArray));
        orderDetailsSenderNameTextView.setText(senderName);
        orderDetailsPickupTimeTextView.setText(pickupTime);
        orderDetailsPickupDateTextView.setText(pickupDate);
        orderDetailsPickupLocationEditText.setText(pickupLocation);
        orderDetailsReceiverNameTextView.setText(receiverName);
        orderDetailsDestinationTextView.setText(destination);
        orderDetailsGoodTypeTextView.setText(goodType);
        orderDetailsVehicleTypeTextView.setText(vehicleType);
        orderDetailsWeightTextView.setText(weight);
        orderDetailsHeightTextView.setText(height);
        orderDetailsWidthTextView.setText(width);
        orderDetailsLengthTextView.setText(length);

        orderDetailsGoodImageImageView.setImageBitmap(Util.getBitmapFromBytesArray(goodImage));
        goodClassificationInfoTextView.setText(goodClassification + " (" + String.format("%.2f%%", goodClassificationConfidence*100) + ")");

        //Set listener for Google Play Button
        googlePayButton.setOnClickListener(this::requestPayment);
    }

    /**
     * If isReadyToPay returned {@code true}, show the button and hide the "checking" text.
     * Otherwise, notify the user that Google Pay is not available. Please adjust to fit in with
     * your current user flow. You are not required to explicitly let the user know if isReadyToPay
     * returns {@code false}.
     *
     * @param available isReadyToPay API response.
     */
    private void setGooglePayAvailable(boolean available) {
        if (available) {
            googlePayButton.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, R.string.googlepay_status_unavailable, Toast.LENGTH_LONG).show();
        }
    }

    public void requestPayment(View view) {

        // Disables the button to prevent multiple clicks.
        googlePayButton.setClickable(false);

        // The price provided to the API should include taxes and shipping.
        // This price is not displayed to the user.
        long dummyPriceCents = 100;
        long shippingCostCents = 900;
        long totalPriceCents = dummyPriceCents + shippingCostCents;
        final Task<PaymentData> task = model.getLoadPaymentDataTask(totalPriceCents);

        // Shows the payment sheet and forwards the result to the onActivityResult method.
        AutoResolveHelper.resolveTask(task, this, LOAD_PAYMENT_DATA_REQUEST_CODE);
    }

    /**
     * Handle a resolved activity from the Google Pay payment sheet.
     *
     * @param requestCode Request code originally supplied to AutoResolveHelper in requestPayment().
     * @param resultCode  Result code returned by the Google Pay API.
     * @param data        Intent from the Google Pay API containing payment or error data.
     * @see <a href="https://developer.android.com/training/basics/intents/result">Getting a result
     * from an Activity</a>
     */
    @SuppressWarnings("deprecation")
    // Suppressing deprecation until `registerForActivityResult` can be used with the Google Pay API.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // value passed in AutoResolveHelper
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {

                    case AppCompatActivity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        handlePaymentSuccess(paymentData);
                        break;

                    case AppCompatActivity.RESULT_CANCELED:
                        // The user cancelled the payment attempt
                        break;

                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        handleError(status);
                        break;
                }

                // Re-enables the Google Pay payment button.
                googlePayButton.setClickable(true);
        }
    }

    /**
     * PaymentData response object contains the payment information, as well as any additional
     * requested information, such as billing and shipping address.
     *
     * @param paymentData A response object returned by Google after a payer approves payment.
     * @see <a href="https://developers.google.com/pay/api/android/reference/
     * object#PaymentData">PaymentData</a>
     */
    private void handlePaymentSuccess(@Nullable PaymentData paymentData) {
        final String paymentInfo = paymentData.toJson();

        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".

            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
            final String token = tokenizationData.getString("token");
            final JSONObject info = paymentMethodData.getJSONObject("info");
            final String billingName = info.getJSONObject("billingAddress").getString("name");
            Toast.makeText(
                    this, getString(R.string.payments_show_name, billingName),
                    Toast.LENGTH_LONG).show();

            // Logging token string.
            Log.d("Google Pay token: ", token);

        } catch (JSONException e) {
            throw new RuntimeException("The selected garment cannot be parsed from the list of elements");
        }
    }

    /**
     * At this stage, the user has already seen a popup informing them an error occurred. Normally,
     * only logging is required.
     *
     * @param status will hold the value of any constant from CommonStatusCode or one of the
     *               WalletConstants.ERROR_CODE_* constants.
     * @see <a href="https://developers.google.com/android/reference/com/google/android/gms/wallet/
     * WalletConstants#constant-summary">Wallet Constants Library</a>
     */
    private void handleError(@Nullable Status status) {
        String errorString = "Unknown error.";
        if (status != null) {
            int statusCode = status.getStatusCode();
            errorString = String.format(Locale.getDefault(), "Error code: %d", statusCode);
        }

        Log.e("loadPaymentData failed", errorString);
    }

    //Create the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    //Define actions for selected options menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent mainActivityIntent = new Intent(CheckoutActivity.this, HomeActivity.class);
                startActivity(mainActivityIntent);
                return true;
            case R.id.action_account:
                Intent accountActivityIntent = new Intent(CheckoutActivity.this, AccountActivity.class);
                startActivity(accountActivityIntent);
                return true;
            case R.id.action_orders:
                Intent myOrdersActivity = new Intent(CheckoutActivity.this, MyOrdersActivity.class);
                startActivity(myOrdersActivity);
                return true;
            case R.id.action_logout:
                Intent mainActivity = new Intent(CheckoutActivity.this, MainActivity.class);
                startActivity(mainActivity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}