
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.sellspot.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

class AdFragment : Fragment() {

    private lateinit var adView: AdView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.add_layout, container, false)

        // Find the AdView in the layout
        adView = view.findViewById(R.id.adView)

        // Load and display the AdView
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        adView.destroy()
    }
}
