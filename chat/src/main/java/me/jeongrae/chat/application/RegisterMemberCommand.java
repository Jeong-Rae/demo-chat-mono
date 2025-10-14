package me.jeongrae.chat.application;

/**
 * 회원 가입을 위한 데이터 커맨드 객체입니다.
 *
 * @param username 사용자 이름
 * @param nickname 닉네임
 * @param password 비밀번호 원문
 */
public record RegisterMemberCommand(String username, String nickname, String password) {
}
