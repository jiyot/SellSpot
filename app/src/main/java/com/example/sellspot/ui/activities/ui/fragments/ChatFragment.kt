package com.example.sellspot.ui.activities.ui.fragments

import com.example.sellspot.firebase.FirebaseClass
import Chat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sellspot.R
import com.example.sellspot.ui.activities.ui.adapters.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ChatFragment : Fragment() {

    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<Chat>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference
    private lateinit var firebaseClass: FirebaseClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().reference
        firebaseClass = FirebaseClass()

        userList = ArrayList()
        adapter = UserAdapter(requireContext(), userList)

        // Use RecyclerView
        userRecyclerView = view.findViewById(R.id.userRecyclerView)
        userRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerView.adapter = adapter

        // Realtime database load
        mDbRef.child("chat_user").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(Chat::class.java)
                    if (mAuth.currentUser?.uid != currentUser?.uid) {
                        userList.add(currentUser!!)
                        // Fetch profile image URL for this user
                        currentUser.uid?.let { fetchAndSetProfileImageURL(it, currentUser) }
                    }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })

        return view
    }

    private fun fetchAndSetProfileImageURL(uid: String, chatUser: Chat) {
        firebaseClass.getUserProfileImageURL(uid) { profileImageURL ->
            chatUser.profileImageURL = profileImageURL
            adapter.notifyDataSetChanged()
        }
    }
}
