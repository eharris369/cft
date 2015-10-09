# Eclipse Tools for Cloud Foundry
  
  Eclipse Tools for Cloud Foundry (CFT) is a framework that provides first-class support for the [Cloud Foundry
  PaaS] (http://docs.cloudfoundry.org/). It allows users to directly deploy applications from their Eclipse
  workspace to a running Cloud Foundry server instance, as well as view and manage deployed applications and services.
  
  Cloud Foundry vendors can also contribute their own Cloud Foundry target definitions for users of the tools
  through various extension points provided by the framework.
  
  CFT is a project under [Eclipse Cloud Development](https://projects.eclipse.org/projects/ecd): 
  
  https://projects.eclipse.org/projects/ecd.cft
  
## IMPORTANT NOTE: Breaking Bundle and Extension Point Changes
  
  All bundle names and extension point IDs have been renamed, therefore new versions of CFT are NOT be backward compatible with any older versions 
  of the plug-in, previously known as Cloud Foundry Eclipse, that used bundle names starting with:
  
  org.cloudfoundry.ide.eclipse.server
  
  Any older versions of the tools need to be manually uninstalled FIRST before installing CFT. This applies to Cloud Foundry Eclipse version 1.8.3 and older.
  
## CLA and Third-Party Pull Requests
  
  All third-party contributors will be required to sign a CLA before Pull Requests are merged in the new repository, pending review and acceptance of the submitted Pull Request.
  
## Update Sites
  
  CFT has not yet been released, therefore only nightly drivers can be installed using this URL:
    
  http://dist.springsource.com/snapshot/TOOLS/cloudfoundry/nightly
  
  (place this URL into the "Install New Software" dialog of your Eclipse)
  
## Raising Bugs and Feature Requests

  Bugs and feature requests should be raised via bugzilla:
  
  https://bugs.eclipse.org/bugs/

## Installation

  Java 7 is a minimum execution environment required to install and run CFT.
  Please make sure your Eclipse is using a Java 7 or higher.

## Getting started

  The basic steps for using CFT are described here:

  http://docs.cloudfoundry.org/buildpacks/java/sts.html

  Note that this description is targeted at users of the Spring Tool Suite, but similar steps apply for
  other Eclipse JEE users.
  

## Building the project
  
  The CFT uses Maven Tycho to do continuous integration builds and
  to produce p2 repos and update sites. To build the tooling yourself, you can execute:

  mvn -Pe43 package
