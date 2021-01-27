package physic

import eventBus.*
import org.jbox2d.callbacks.*
import org.jbox2d.collision.*
import org.jbox2d.dynamics.contacts.*
import kotlin.math.*

class CustomContactListener(private val bus: EventBus) : ContactListener {
    override fun beginContact(contact: Contact) {

        val fixtureA = contact.m_fixtureA
        val fixtureB = contact.m_fixtureB

        when (fixtureA?.userData){
            Hit_Type.Player ->{
                when (fixtureB?.userData){
                    Hit_Type.Player ->{}
                    Hit_Type.Bullet ->{}
                    Hit_Type.Ground ->{bus.send(GroundedEvent())}
                    Hit_Type.Platform ->{
                        val angle = (atan2(fixtureB.m_body!!.linearVelocityY,fixtureB.m_body!!.linearVelocityX)*180/ PI)
                        if (190>angle&&angle<350){
                            contact.isEnabled=false
                        }else bus.send(GroundedEvent())
                    }
                }
            }
            Hit_Type.Bullet ->{
                when (fixtureB?.userData){
                    Hit_Type.Player ->{}
                    Hit_Type.Bullet ->{}
                    Hit_Type.Ground ->{}
                    Hit_Type.Platform ->{}
                }
            }
            Hit_Type.Ground ->{
                when (fixtureB?.userData){
                    Hit_Type.Player ->{bus.send(GroundedEvent())}
                    Hit_Type.Bullet ->{}
                    Hit_Type.Ground ->{}
                    Hit_Type.Platform ->{}
                }
            }
            Hit_Type.Platform ->{
                when (fixtureB?.userData){
                    Hit_Type.Player ->{
                        val angle = (atan2(fixtureB.m_body!!.linearVelocityY,fixtureB.m_body!!.linearVelocityX)*180/ PI)
                        if (190>angle&&angle<350){
                            contact.isEnabled=false
                        }else bus.send(GroundedEvent())
                    }
                    Hit_Type.Bullet ->{}
                    Hit_Type.Ground ->{}
                    Hit_Type.Platform ->{}
                }
            }
        }
    }

    override fun endContact(contact: Contact) {

    }

    override fun postSolve(contact: Contact, impulse: ContactImpulse) {

    }

    override fun preSolve(contact: Contact, oldManifold: Manifold) {

    }
}