package com.mariabuliga.softpaws.presentation.screens

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mariabuliga.softpaws.R
import com.mariabuliga.softpaws.data.model.CatDataItem
import com.mariabuliga.softpaws.databinding.FragmentCatDetailsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CatDetailsFragment : Fragment() {

    private var _binding: FragmentCatDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCatDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getSerializable("catItem", CatDataItem::class.java)
        } else {
            arguments?.getSerializable("catItem") as? CatDataItem
        }
        cat?.let {
            populateDetails(it)
        }
    }

    fun populateDetails(cat: CatDataItem) {
        binding.catDetails.nameValue.text = cat.name
        binding.catDetails.originValue.text = cat.origin
        binding.catDetails.temperamentValue.text = cat.temperament
        binding.catDetails.lifespanValue.text = cat.life_span
        binding.catDetails.intelligenceRating.rating = cat.intelligence?.toFloat()!!
        binding.catDetails.affectionLevelRating.rating = cat.affection_level?.toFloat()!!
        binding.catDetails.childFriendlyRating.rating = cat.child_friendly?.toFloat()!!
        binding.catDetails.socialNeedsRating.rating = cat.social_needs?.toFloat()!!
        binding.catDescription.text = cat.description
        binding.wikipediaUrl.text = cat.wikipedia_url
        binding.vetStreetUrl.text = cat.vetstreet_url

        cat.image?.url?.let {
            Glide.with(requireContext())
                .load(cat.image.url)
                .placeholder(R.drawable.pet_icon_light_grey)
                .error(R.drawable.pet_icon_light_grey)
                .into(binding.catImage)
        }

        binding.wikipediaUrl.setOnClickListener {
            val url = cat.wikipedia_url
            url?.let { openUrl(it) }
        }

        binding.vetStreetUrl.setOnClickListener {
            val url = cat.vetstreet_url
            url?.let { openUrl(it) }
        }
    }

    private fun openUrl(url: String) {
        val urlIntent = Intent(Intent.ACTION_VIEW, url.toUri())
        if (urlIntent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(urlIntent)
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_app_installed),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}