//////////////////////////////////////////////////////////////////////////////////////////
//	Scene.cpp
//	Draw the scene for shadow mapping
//	Downloaded from: www.paulsprojects.net
//	Created:	16th September 2003
//
//	Copyright (c) 2006, Paul Baker
//	Distributed under the New BSD Licence. (See accompanying file License.txt or copy at
//	http://www.paulsprojects.net/NewBSDLicense.txt)
//////////////////////////////////////////////////////////////////////////////////////////	
#define WIN32_LEAN_AND_MEAN
#include <windows.h>
#include <stdio.h>
#include <GL/glut.h>
#include "Maths/Maths.h"
#include "scene.h"

void DrawScene(float angle)
{

	//Display lists for objects
	static GLuint spheresList=0, torusList=0, baseList=0;

	//Create spheres list if necessary
	if(!spheresList)
	{
		spheresList=glGenLists(1);
		glNewList(spheresList, GL_COMPILE);
		{
			glColor3f(0.0f, 1.0f, 0.0f);
			glPushMatrix();

			glTranslatef(0.45f, 1.0f, 0.45f);
			glutSolidSphere(0.2, 24, 24);

			glTranslatef(-0.9f, 0.0f, 0.0f);
			glutSolidSphere(0.25, 24, 24);

			glColor4f(0.0f, .0f, 1.0f,0.01f);
		//	glEnable(GL_BLEND);
			
		//	glBlendFunc(GL_SRC_ALPHA, GL_ONE);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			glutSolidCube(0.5f);
			
			glDisable(GL_BLEND);
			glColor3f(1.0f, 1.0f, 1.0f);
			
			glTranslatef(0.0f, 0.0f,-0.9f);
			glutSolidSphere(0.2, 24, 24);

			glTranslatef(0.9f, 0.0f, 0.0f);
			//glutSolid
			glutSolidSphere(0.2, 24, 24);

			glPopMatrix();
		}
		glEndList();
	}

	//Create torus if necessary
	if(!torusList)
	{
		torusList=glGenLists(1);
		glNewList(torusList, GL_COMPILE);
		{
			
			glPushMatrix();

			glRotated(-90.0, 1.0f, 0.0f, 0.0);
			glColor3f(1.0, 0.0, 0.0);
			glutSolidCone(0.3, 1.0, 60, 50);

			glEnable(GL_BLEND);
			glBlendFunc(GL_ONE_MINUS_DST_ALPHA, GL_SRC_COLOR);
			glColor3f(.0f, 0.0f, 1.0f);
			GLUquadricObj* cone;
			cone = gluNewQuadric();
			gluQuadricDrawStyle(cone, GLU_LINE);
		//gluQuadricTexture(cone, GL_TRUE);

		//	gluQuadricNormals(cone, GLU_SMOOTH);
		
			glEnable(GL_TEXTURE_2D);
			
			gluCylinder(cone, 0.4, 0.4, 2.5, 60, 60);
			//glBindTexture(cone, );
			glDisable(GL_BLEND);
			
			
	        glPopMatrix();

			glPushMatrix();
			glRotated(-90.0, 1.0f, 0.0f, 0.0);

			
			glPopMatrix();

		}
		glEndList();
	}

	//Create base if necessary
	if(!baseList)
	{
		baseList=glGenLists(1);
		glNewList(baseList, GL_COMPILE);
		{
			glColor3f(1.0f, 1.0f, 1.0f);
			glPushMatrix();

			glScalef(1.0f, 0.05f, 1.0f);
			glutSolidCube(3.0f);
			
			glTranslatef(-1.45f, 25.5f, 0.0f);

			glScalef(0.05f, 18.0f, 1.0f);
			glutSolidCube(3.0f);


			glColor3f(1.0f, 1.0f, 1.0f);
			glRotatef(90.0f,0.0f,1.0f,0.0f);
			glTranslatef(-1.5f, 0.0f, 29.5f);
			glScalef(0.05f, 1.0f, 20.5f);
			glutSolidCube(3.0f);

			glColor3f(1.0f, 1.0f, 1.0f);
			glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
			glTranslatef(-1.5f, 0.0f, 29.5f);
			glScalef(0.05f, 1.0f, 20.5f);
			glutSolidCube(3.0f);

			glColor3f(1.0f, 1.0f, 1.0f);
			glRotatef(90.0f, .0f, 1.0f, 0.0f);
			glRotatef(-90.0f, .0f, .0f, 1.0f);
			glTranslatef(-1.5f, 0.0f, 29.5f);
			glScalef(0.05f, 1.0f, 20.05f);
			glutSolidCube(3.0f);


			glPopMatrix();
		}
		glEndList();
	}


	//Draw objects
	glCallList(baseList);
	glPushMatrix();
	glEnable(GL_BLEND);
	glBlendFunc(GL_ONE_MINUS_DST_ALPHA, GL_SRC_COLOR);
	
	glCallList(torusList);
	glDisable(GL_BLEND);
	glPushMatrix();
	glRotatef(angle, 0.0f, 1.0f, 0.0f);
	
	glCallList(spheresList);
	glPopMatrix();

	
}



