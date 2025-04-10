package com.anhoang.socialnetworkdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", indexes = {
		@Index(name = "idx_users_user_code", columnList = "user_code")
})
public class Users extends BaseEntity<Long> implements Serializable {
	@Column(columnDefinition = "VARCHAR(12)", unique = true)
	private String userCode;
	@Column(columnDefinition = "VARCHAR(100)", nullable = false)
	private String fullName;
	@Column(columnDefinition = "TEXT")
	private String avatar;
	@Column(columnDefinition = "TEXT")
	private String coverImage;
	@Column(columnDefinition = "DATE")
	private LocalDate dob;
	@Enumerated(EnumType.STRING)
	@Column
	private Gender gender;
	@Column(columnDefinition = "VARCHAR(15)")
	private String phoneNumber;
	@Column(columnDefinition = "VARCHAR(150)", unique = true, nullable = false)
	private String email;
	@Column(columnDefinition = "TEXT")
	private String bio;
	@Column(columnDefinition = "VARCHAR(255)", nullable = false)
	private String password;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;
	@Column(columnDefinition = "VARCHAR(64)")
	private String verifyCode;
	@Column(columnDefinition = "VARCHAR(64)")
	private String refreshToken;

	//Post-Comment-Reaction
	@OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Post> postList = new ArrayList<>();
	@OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<PostComment> commentList = new ArrayList<>();
	@OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<PostReaction> postReactionList = new ArrayList<>();

	//Conversation-Message
	@OneToMany(mappedBy = "users", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<ConversationMember> conversationMemberList;
	@OneToMany(mappedBy = "senderUsers", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Message> messageList;
	@OneToMany(mappedBy = "usersUpdated", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<MessageFile> messageFileList;

	//MediaFile
	@OneToMany(mappedBy = "usersUpdated", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<MediaFile> mediaFileList;

	//Follow-User
	@OneToMany(mappedBy = "follower", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Follow> userMeFollowList;
	@OneToMany(mappedBy = "following", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Follow> userFollowMeList;

	//Friendship
	@OneToMany(mappedBy = "user", fetch =  FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Friendship> userList = new ArrayList<>(); //danh sach ban duoc gui
	@OneToMany(mappedBy = "friend", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Friendship> friendList = new ArrayList<>(); //Danh sach ban nguoi dung gui

	//Group
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private  List<GroupMember> groupList = new ArrayList<>();

	//Role
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(
			name = "user_roles",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private List<Roles> rolesList = new ArrayList<>();

	public enum Gender{
		NAM,
		NU;
	}

	public enum Status{
		BINH_THUONG,
		CHUA_XAC_NHAN,
		KHOA,
		DA_XOA,
		CANH_BAO
	}

	@PostPersist
	public void updateUserCode() {
		if(this.userCode==null){
			this.userCode = "AC" + String.format("%08d", this.getId());
		}
	}
}
