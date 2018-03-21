package ch.epfl.imhof.osm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.imhof.Attributes;
/**
 * Represents an OSMRelation
 * 
 * @author Mortiniera Thevie (234914)
 * @author Leimgruber Isaac (236908)
 */
public final class OSMRelation extends OSMEntity {
    private final List<Member> members;
    /**
     * @param id
     *      relation's id
     * @param members
     *      relation's member list
     * @param attributes
     *      relation's attributes
     */
    public OSMRelation(long id, List<Member> members, Attributes attributes) {
        super(id, attributes);
        this.members = Collections.unmodifiableList(new ArrayList<>(members));
    }
    /**
     * @return
     *      the relation's member list
     */
    public List<Member> members() {
        return members;
    }
    /**
     * a relation's Member
     * 
     * @author Mortiniera Thevie (234914)
     * @author Leimgruber Isaac (236908)
     */
    public static final class Member {
        private final Type type;
        private final String role;
        private final OSMEntity member;
        /**
         * @param type
         *      a member's type
         * @param role
         *      a member's role
         * @param member
         *      a member's OSMEntity
         */
        public Member(Type type, String role, OSMEntity member) {
            this.type = type;
            this.role = role;
            this.member = member;
        }
        /**
         * @return
         *      the member's type
         */
        public Type type() {
            return type;
        }
        /**
         * @return
         *      the member's role
         */
        public String role() {
            return role;
        }
        /**
         * @return
         *      the member's OSMEntity
         */
        public OSMEntity member() {
            return member;
        }
        /**
         * possible member's type 
         * 
         * @author Mortiniera Thevie (234914)
         * @author Leimgruber Isaac (236908)
         */
        public enum Type {
            NODE, WAY, RELATION;
        }
    }
    /**
     * a to be built relation
     * 
     * @author Mortiniera Thevie (234914)
     * @author Leimgruber Isaac (236908)
     */
    public static final class Builder extends OSMEntity.Builder {
        private final List<Member> members;
        /**
         * @param id
         *         l'identifiant de la relation
         */
        public Builder(long id) {
            super(id);
            members = new ArrayList<>();
        }
        /**
         * @param type
         *     the to be added lelber's type
         * @param role
         *      his role
         * @param newMember
         *      his OSMEntity
         * adds a member to the relation's member list
         */
        public void addMember(Member.Type type, String role, OSMEntity newMember) {
            members.add(new Member(type, role, newMember));
        }
        /**
         * @return
         *      a new OSMRelation built with the last current to be built relation's
         * @throws IllegalStateException
         *      if the to be built relation has been set incomplete
         */
        public OSMRelation build() {
            if (isIncomplete()) 
                throw new IllegalStateException("relation is incomplete");

            return new OSMRelation(id, members, attributesBuilder.build());
        }
    }
}
