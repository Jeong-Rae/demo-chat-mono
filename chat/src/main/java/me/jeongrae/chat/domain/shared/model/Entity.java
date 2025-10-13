package me.jeongrae.chat.domain.shared.model;

import lombok.Getter;
import lombok.experimental.Accessors;
import me.jeongrae.chat.common.guard.Guard;

/**
 * 모든 도메인 엔티티의 공통 기반 클래스.
 * - 플루언트 스타일 접근자 사용
 * - 식별자 보유 및 동일성 비교
 */
@Getter
@Accessors(fluent = true)
public abstract class Entity<ID> implements Identifiable<ID> {
    protected final ID id;

    protected Entity(ID id) {
        this.id = Guard.notNull(id, "id는 null일 수 없습니다.");
    }

    @Override
    public ID id() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Entity<?> other = (Entity<?>) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
