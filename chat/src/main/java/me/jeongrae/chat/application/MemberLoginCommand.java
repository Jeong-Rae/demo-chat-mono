package me.jeongrae.chat.application;

/**
 * 멤버 로그인을 위한 데이터 커맨드 객체입니다.
 *
 * @param username 사용자 이름
 * @param password 비밀번호
 */
public record MemberLoginCommand(String username, String password) {
}
