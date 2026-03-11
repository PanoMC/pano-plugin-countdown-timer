import {PanoPlugin, viewComponent} from '@panomc/sdk';
import {derived} from 'svelte/store';
import {_ as i18n} from '@panomc/sdk/utils/language';
import ApiUtil from '@panomc/sdk/utils/api';

const pluginId = 'pano-plugin-countdown-timer';

// this is to render plugin translations
export const _ = derived(i18n, ($_fn) => {
  return (key, options) => $_fn(`plugins.${pluginId}.${key}`, options);
});

export default class CountdownTimerPlugin extends PanoPlugin {
  onLoad() {
    const pano = this.pano;

    if (pano.isPanel) {
      // Load config when visiting the plugin detail page
      pano.ui.addon.onLoad(async (data, event) => {
        if (data.addon.id !== pluginId) return;

        try {
          const config = await ApiUtil.get({
            path: '/api/panel/countdown-timer/config',
            request: event,
          });
          data.addon.config = config;
        } catch (e) {
          console.error('[pano-plugin-countdown-timer] Failed to load config', e);
        }
      });

      // Register settings component in plugin detail
      pano.ui.hook.register({
        name: `panel:plugin-detail:content:${pluginId}`,
        component: viewComponent(() => import('./panel/components/view/CountdownTimerSettings.svelte')),
        permission: `pano.plugin.${pluginId}.manage.countdown.timer`,
      });
    } else {
      // Theme side - load config for countdown display
      const countdownCoverComponent = viewComponent(() => import('./theme/components/view/CountdownTimerCover.svelte'));
      const countdownPageComponent = viewComponent(() => import('./theme/components/view/CountdownTimerPage.svelte'));
      const countdownSidebarComponent = viewComponent(() => import('./theme/components/view/CountdownTimerSidebar.svelte'));

      let registered = false;

      pano.ui.app.onLoad(async (data, event) => {
        try {
          const config = await ApiUtil.get({
            path: '/api/countdown-timer/config',
            request: event,
          });
          data.countdownTimerConfig = config;

          // Register page and nav link only once
          if (!registered) {
            registered = true;

            if (config.displayMode === 'PAGE') {
              const pagePath = config.pagePath || '/countdown';
              pano.ui.page.register({
                path: pagePath,
                component: countdownPageComponent,
              });

              if (config.enabled && config.showInNav) {
                pano.ui.nav.site.editNavLinks((links) => {
                  links.push({
                    href: pagePath,
                    text: config.navLinkText || config.title || 'Countdown',
                  });
                  return links;
                });
              }
            }
          }
        } catch (e) {
          console.error('[pano-plugin-countdown-timer] Failed to load config', e);
        }
      });

      // Register cover overlay at theme:top (same as cookies banner)
      pano.ui.hook.register({
        name: 'theme:top',
        component: countdownCoverComponent,
        skipLoad: true,
      });

      // Register countdown widget in home sidebar
      pano.ui.sidebar.register({
        sidebarId: 'home',
        id: 'countdown-timer',
        component: countdownSidebarComponent,
        priority: 90,
      });
    }
  }

  onContextUpdate(ctx) { }

  onUnload() { }
}
