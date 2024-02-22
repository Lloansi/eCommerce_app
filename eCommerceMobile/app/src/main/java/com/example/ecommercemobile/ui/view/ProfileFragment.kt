package com.example.ecommercemobile.ui.view

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.ecommercemobile.R
import com.example.ecommercemobile.databinding.FragmentProfileBinding
import com.example.ecommercemobile.ui.view.dialogs.LogOutBottomSheet
import com.example.ecommercemobile.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.io.File

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    lateinit var binding: FragmentProfileBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var prefs: SharedPreferences
    private var imagePath = ""
    private var imageUri: Uri? = null
    private val REQUEST_PERMISSIONS = 1
    private var newImageName = ""
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            if (data != null) {
                runBlocking {
                    imageUri = data.data!!
                    binding.profileIV.setImageURI(imageUri)
                    // Get the file name from the URI
                    newImageName = getFileName(imageUri!!)
                }
                if (imageUri != null) {
                    try {
                        imagePath = getPathFromUri(imageUri!!)!!
                        val imageFile = File(imagePath)
                        userViewModel.putUserPicture(imageFile)
                    } catch (e: java.lang.NullPointerException){
                        Log.e("Error ", e.message, e.cause)
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        println("PROFILE ON CREATE VIEW")
        userViewModel = ViewModelProvider(requireActivity())[UserViewModel::class.java]
        binding = FragmentProfileBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.photoBT.setOnClickListener {
            if(ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 10)
            }
            selectImage()

        }

        binding.usernameTV.text = userViewModel.user.value?.userEmail
        userViewModel.userImage.observe(viewLifecycleOwner){ userImage ->
            if (userImage != null){
                binding.profileIV.setImageBitmap(userImage)
            } else binding.profileIV.setImageResource(R.drawable.placeholder)
        }
        userViewModel.user.observe(viewLifecycleOwner){ user ->
            if (user!=null){
                binding.usernameTV.text = user.userEmail
            }
        }
        binding.logoutLY.setOnClickListener {
            val modalBottomSheet = LogOutBottomSheet()
            modalBottomSheet.show(requireParentFragment().parentFragmentManager, LogOutBottomSheet.TAG)
        }
        binding.editProfileLY.setOnClickListener {
            val toEditAccount = ProfileFragmentDirections.actionProfileFragmentToEditAccountFragment()
            findNavController().navigate(toEditAccount)
        }
        binding.ordersLY.setOnClickListener {
            val toOrders = ProfileFragmentDirections.actionProfileFragmentToOrdersFragment()
            findNavController().navigate(toOrders)
        }

        binding.helpcenterLY.setOnClickListener{
            val toHelp = ProfileFragmentDirections.actionProfileFragmentToHelpFragment()
            findNavController().navigate(toHelp)
        }

        prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        // If there's no value for darkmode, set it to false
        binding.darkmodeSwitch.isChecked = prefs.getBoolean("darkmode", false)

        binding.darkmodeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                prefs.edit()
                    .putBoolean("darkmode",true)
                    .apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                println("Darkmode is on")
            } else {
                prefs.edit()
                .putBoolean("darkmode",false)
                .apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                println("Darkmode is off")
            }
            requireActivity().recreate()
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context?.contentResolver?.query(uri,
                null, null, null, null)
            cursor?.let {
                if (it.moveToFirst()) {
                    val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
                    result = cursor.getString(column_index)
                }
                cursor.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                result = result?.substring(cut!! + 1)
            }
        }
        return result!!
    }

    private fun getPathFromUri(uri: Uri): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = requireActivity().contentResolver.query(uri, projection, null, null, null)
        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val imagePath = cursor.getString(columnIndex)
        cursor.close()
        return imagePath
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    selectImage()
                } else {
                    Toast.makeText(requireContext(),
                        "Please accept the permissions.", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }
}